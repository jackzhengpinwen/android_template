package com.example.core_common.view.global

import android.R
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

object Gloading {
    const val STATUS_LOADING = 1
    const val STATUS_LOAD_SUCCESS = 2
    const val STATUS_LOAD_FAILED = 3
    const val STATUS_EMPTY_DATA = 4
    const val STATUS_NORMAL = 5 //和Loading状态一样 默认先展示Loading的布局


    @Volatile
    private lateinit var mDefault: Gloading
    private var mAdapter: Adapter? = null
    private var DEBUG = false

    /**
     * Provides view to show current icon_loading status
     */
    interface Adapter {
        /**
         * get view for current status
         *
         * @param holder      Holder
         * @param convertView The old view to reuse, if possible.
         * @param status      current status
         * @return status view to show. Maybe convertView for reuse.
         * @see Holder
         */
        fun getView(holder: Holder?, convertView: View?, status: Int, message: String?): View?
    }

    /**
     * set debug mode or not
     *
     * @param debug true:debug mode, false:not debug mode
     */
    fun debug(debug: Boolean) {
        DEBUG = debug
    }

    private fun Gloading(): Gloading {
        return this
    }

    /**
     * Create a new Gloading different from the default one
     *
     * @param adapter another adapter different from the default one
     * @return Gloading
     */
    fun from(adapter: Adapter?): Gloading? {
        val gloading = Gloading()
        gloading.mAdapter = adapter
        return gloading
    }

    /**
     * get default Gloading object for global usage in whole app
     *
     * @return default Gloading object
     */
    fun getDefault(): Gloading {
        if (mDefault == null) {
            synchronized(Gloading::class.java) {
                if (mDefault == null) {
                    mDefault = Gloading()
                }
            }
        }
        return mDefault
    }

    /**
     * init the default icon_loading status view creator ([Adapter])
     *
     * @param adapter adapter to create all status views
     */
    fun initDefault(adapter: Adapter?) {
        getDefault()!!.mAdapter = adapter
    }

    /**
     * Gloading(icon_loading status view) wrap the whole activity
     * wrapper is android.R.id.content
     *
     * @param activity current activity object
     * @return holder of Gloading
     */
    fun wrap(activity: Activity): Holder {
        val wrapper = activity.findViewById<ViewGroup>(R.id.content)
        return Holder(mAdapter, activity, wrapper)
    }

    /**
     * Gloading(icon_loading status view) wrap the specific view.
     *
     * @param view view to be wrapped
     * @return Holder
     */
    fun wrap(view: View): Holder? {
        val wrapper = FrameLayout(view.context)
        val lp = view.layoutParams
        if (lp != null) {
            wrapper.layoutParams = lp
        }
        if (view.parent != null) {
            val parent = view.parent as ViewGroup
            val index = parent.indexOfChild(view)
            parent.removeView(view)
            parent.addView(wrapper, index)
        }
        val newLp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        wrapper.addView(view, newLp)
        val holder = Holder(mAdapter, view.context, wrapper)
        //2020-12-17 Newki修改
        //主要添加了GLoading那么就默认显示Loading状态 ，不然会先出现默认布局，然后一闪之后再出现GLoading的加载
        holder.showNormal()
        return holder
    }

    /**
     * loadingStatusView shows cover the view with the same LayoutParams object
     * this method is useful with RelativeLayout and ConstraintLayout
     *
     * @param view the view which needs show icon_loading status
     * @return Holder
     */
    fun cover(view: View): Holder? {
        val parent = view.parent
            ?: throw RuntimeException("view has no parent to show gloading as cover!")
        val viewGroup = parent as ViewGroup
        val wrapper = FrameLayout(view.context)
        viewGroup.addView(wrapper, view.layoutParams)
        return Holder(mAdapter, view.context, wrapper)
    }

    /**
     * Gloading holder<br></br>
     * create by [Gloading.wrap] or [Gloading.wrap]<br></br>
     * the core API for showing all status view
     */
    class Holder constructor(
        private val mAdapter: Adapter?,
        context: Context,
        wrapper: ViewGroup
    ) {
        val context: Context?

        /**
         * get retry task
         *
         * @return retry task
         */
        var retryTask: Runnable? = null
            private set
        private var mCurStatusView: View? = null

        /**
         * get wrapper
         *
         * @return container of gloading
         */
        val wrapper: ViewGroup?
        private var curState = 0
        private val mStatusViews = SparseArray<View>(4)
        private var mData: Any? = null

        init {
            this.context = context
            this.wrapper = wrapper
        }

        /**
         * set retry task when user click the retry button in load failed page
         *
         * @param task when user click in load failed UI, run this task
         * @return this
         */
        fun withRetry(task: Runnable?): Holder {
            retryTask = task
            return this
        }

        /**
         * set extension data
         *
         * @param data extension data
         * @return this
         */
        fun withData(data: Any?): Holder {
            mData = data
            return this
        }

        /**
         * show UI for status: [.STATUS_NORMAL]
         */
        fun showNormal() {
            showLoadingStatus(STATUS_NORMAL, null)
        }

        /**
         * show UI for status: [.STATUS_LOADING]
         */
        fun showLoading() {
            showLoadingStatus(STATUS_LOADING, null)
        }

        /**
         * show UI for status: [.STATUS_LOAD_SUCCESS]
         */
        fun showLoadSuccess() {
            showLoadingStatus(STATUS_LOAD_SUCCESS, null)
        }

        /**
         * show UI for status: [.STATUS_LOAD_FAILED]
         */
        fun showLoadFailed(msg: String?) {
            showLoadingStatus(STATUS_LOAD_FAILED, msg)
        }

        /**
         * show UI for status: [.STATUS_EMPTY_DATA]
         */
        fun showEmpty() {
            showLoadingStatus(STATUS_EMPTY_DATA, null)
        }

        /**
         * Show specific status UI
         *
         * @param status status
         * @see .showLoading
         * @see .showLoadFailed
         * @see .showLoadSuccess
         * @see .showEmpty
         */
        fun showLoadingStatus(status: Int, msg: String?) {
            if (curState == status || !validate()) {
                return
            }
            curState = status
            //first try to reuse status view
            var convertView = mStatusViews[status]
            if (convertView == null) {
                //secondly try to reuse current status view
                convertView = mCurStatusView
            }
            try {
                //call customer adapter to get UI for specific status. convertView can be reused
                val view = mAdapter!!.getView(this, convertView, status, msg)
                if (view == null) {
                    printLog(mAdapter.javaClass.name + ".getView returns null")
                    return
                }
                if (view !== mCurStatusView || wrapper!!.indexOfChild(view) < 0) {
                    if (mCurStatusView != null) {
                        wrapper!!.removeView(mCurStatusView)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.elevation = Float.MAX_VALUE
                    }
                    wrapper!!.addView(view)
                    val lp = view.layoutParams
                    if (lp != null) {
                        lp.width = ViewGroup.LayoutParams.MATCH_PARENT
                        lp.height = ViewGroup.LayoutParams.MATCH_PARENT
                    }
                } else if (wrapper.indexOfChild(view) != wrapper.childCount - 1) {
                    // make sure icon_loading status view at the front
                    view.bringToFront()
                }
                mCurStatusView = view
                mStatusViews.put(status, view)
            } catch (e: Exception) {
                if (DEBUG) {
                    e.printStackTrace()
                }
            }
        }

        private fun validate(): Boolean {
            if (mAdapter == null) {
                printLog("Gloading.Adapter is not specified.")
            }
            if (context == null) {
                printLog("Context is null.")
            }
            if (wrapper == null) {
                printLog("The mWrapper of icon_loading status view is null.")
            }
            return mAdapter != null && context != null && wrapper != null
        }

        /**
         * get extension data
         *
         * @param <T> return type
         * @return data
        </T> */
        fun <T> getData(): T? {
            try {
                return mData as T?
            } catch (e: Exception) {
                if (DEBUG) {
                    e.printStackTrace()
                }
            }
            return null
        }
    }

    private fun printLog(msg: String) {
        if (DEBUG) {
            Log.e("Gloading", msg)
        }
    }
}