package com.example.core_common.view.titlebar

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.core_common.R
import com.example.core_common.utils.CommUtils
import com.example.core_common.utils.ThreadPoolUtils

class EasyTitleBar : RelativeLayout {

    companion object {
        private lateinit var titleBarSetting: TitleBarSetting

        open fun init(): TitleBarSetting {
            titleBarSetting = TitleBarSetting()
            return titleBarSetting
        }
    }

    private lateinit var title_tv: TextView
    private lateinit var titleLayout: LinearLayout
    private lateinit var fit_cl: ConstraintLayout

    //返回箭头图片
    lateinit var backImage: ImageView

    //返回箭头的父布局
    private lateinit var backLayout: LinearLayout
    private lateinit var rightLayout: ViewGroup
    private lateinit var leftLayout: ViewGroup
    private lateinit var title_vg: ViewGroup

    //分割线
    private var titleLine: View? = null

    //menu图片大小
    private var menuImgSize = 0f

    //menu文字大小
    private var menuTextSize = 0f

    //menu文字颜色
    private var menuTextColor = 0

    //标题栏高度
    private var titleBarHeight = 0f

    //标题栏背景
    private var titleBarBackGround = 0

    //填充状态栏的颜色
    private var fitColor = 0

    //左边的图标（一般为返回箭头）
    private var backRes = 0

    //返回箭头、左右viewgroup距两边的距离
    private var parentPadding = 0f

    //左右viewgroup之间的距离
    private var viewPadding = 0f

    //标题字体大小
    private var titleTextSize = 0f

    //标题字体颜色
    private var titleColor = 0

    //标题字排列风格  居中或是居左
    private var titleStyle = 0

    val TITLE_STYLE_LEFT = 1
    val TITLE_STYLE_CENTER = 0

    //分割线高度
    private var lineHeight = 0f

    //分割线颜色
    private var lineColor = 0
    private var backImageSize = 0

    private var onDoubleClickListener: OnDoubleClickListener? = null

    private val leftConstraintSet: ConstraintSet = ConstraintSet()
    private val centerConstraintSet: ConstraintSet = ConstraintSet()
    private var lineState = 0
    private var detector: GestureDetector? = null
    private var title: String? = null
    private var backLayoutState = 1
    private var fitSystemWindow = false
    private var status_view: View? = null

    private var hasStatusPadding = false

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs, defStyle)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0) {
        init(context, attrs, 0)
    }

    constructor(context: Context): super(context) {
        init(context, null, 0)
    }

    fun init(context: Context, attrs: AttributeSet?, defStyle: Int) {
        LayoutInflater.from(context).inflate(R.layout.easy_titlebar, this)
        fit_cl = findViewById<ConstraintLayout>(R.id.fit_cl)
        status_view = findViewById<View>(R.id.status_view)
        backImage = findViewById<ImageView>(R.id.left_image)
        rightLayout = findViewById<ViewGroup>(R.id.right_layout)
        leftLayout = findViewById<ViewGroup>(R.id.left_layout)
        backLayout = findViewById<LinearLayout>(R.id.back_layout)
        title_tv = findViewById<TextView>(R.id.title_tv)
        titleLayout = findViewById<LinearLayout>(R.id.root)
        titleLine = findViewById<View>(R.id.line)
        leftConstraintSet.clone(fit_cl)
        centerConstraintSet.clone(fit_cl)
        initEvent()
        initSetting()
        parseStyle(context, attrs)
    }

    private fun initEvent() {
        detector = GestureDetector(object : SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                if (onDoubleClickListener != null) onDoubleClickListener!!.onDoubleEvent(title_tv)
                return super.onDoubleTap(e)
            }
        })
        titleLayout!!.setOnTouchListener { v, event -> detector!!.onTouchEvent(event) }
        setOnBackListener()
    }

    /**
     * 设置返回的默认事件
     */
    fun setOnBackListener() {
        getBackLayout()!!.setOnClickListener { //走底部系统返回键
            if (ThreadPoolUtils.getCachedThreadPool() != null) {
                ThreadPoolUtils.getCachedThreadPool().execute {
                    try {
                        val inst = Instrumentation()
                        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }


    //初始化
    private fun initSetting() {
        if (titleBarSetting == null) return
        titleBarBackGround = titleBarSetting.getBackgroud()
        backRes = titleBarSetting.getBack_icon()
        titleTextSize = EasyUtil.sp2px(getContext(), titleBarSetting.getTitleSize()).toFloat()
        titleColor = titleBarSetting.getTitleColor()
        titleBarHeight = EasyUtil.dip2px(getContext(), titleBarSetting.getTitleBarHeight()).toFloat()
        parentPadding = EasyUtil.dip2px(getContext(), titleBarSetting.getParentPadding()).toFloat()
        viewPadding = EasyUtil.dip2px(getContext(), titleBarSetting.getViewPadding()).toFloat()
        backImageSize = EasyUtil.dip2px(getContext(), titleBarSetting.getBackImageSize())
        menuImgSize = EasyUtil.dip2px(getContext(), titleBarSetting.getMenuImgSize()).toFloat()
        menuTextColor = titleBarSetting.getMenuTextColor()
        menuTextSize = EasyUtil.sp2px(getContext(), titleBarSetting.getMenuTextSize()).toFloat()
        titleStyle = titleBarSetting.getTitleStyle()
        lineHeight = titleBarSetting.getLineHeight().toFloat()
        lineColor = titleBarSetting.getLineColor()
        fitSystemWindow = titleBarSetting.isFitSystemWindow()
        hasStatusPadding = titleBarSetting.isHasStatusPadding()
        lineState = if (titleBarSetting.getShowLine()) {
            1
        } else {
            0
        }
    }

    private fun parseStyle(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.EasyTitleBar)
            fitSystemWindow =
                ta.getBoolean(R.styleable.EasyTitleBar_Easy_fitsSystemWindows, fitSystemWindow)
            fit_cl.setFitsSystemWindows(fitSystemWindow)
            hasStatusPadding =
                ta.getBoolean(R.styleable.EasyTitleBar_Easy_hasStatusPadding, hasStatusPadding)
            if (hasStatusPadding) {
                val statusParams = status_view!!.layoutParams as LinearLayout.LayoutParams
                statusParams.height = EasyUtil.getStateBarHeight(getContext())
                status_view!!.layoutParams = statusParams
            } else {
                val statusParams = status_view!!.layoutParams as LinearLayout.LayoutParams
                statusParams.height = 0
                status_view!!.layoutParams = statusParams
            }


            //返回箭头
            val backDrawable = ta.getDrawable(R.styleable.EasyTitleBar_Easy_backRes)
            if (backDrawable != null) {
                backImage!!.setImageDrawable(backDrawable)
            } else {
                backImage!!.setImageResource(backRes)
            }


            //标题栏
            titleBarHeight =
                ta.getDimension(R.styleable.EasyTitleBar_Easy_titleBarHeight, titleBarHeight)
            titleBarBackGround =
                ta.getColor(R.styleable.EasyTitleBar_Easy_titleBarBackground, titleBarBackGround)
            titleLayout!!.setBackgroundColor(titleBarBackGround)
            val titleParams = fit_cl.getLayoutParams() as LinearLayout.LayoutParams
            titleParams.height = titleBarHeight.toInt()
            fit_cl.setLayoutParams(titleParams)
            fitColor = titleBarBackGround
            fitColor = ta.getColor(R.styleable.EasyTitleBar_Easy_fitColor, fitColor)
            if (fitColor == Color.WHITE || fitColor == CommUtils.getColor(R.color.white)) {
                fitColor =
                    if (Build.VERSION.SDK_INT < 23) CommUtils.getColor(R.color.status_bar_gray_bg) else Color.WHITE
            }
            //设置EasyTitleBar中状态栏的颜色
            setFitColor(fitColor)

            //标题
            title = ta.getString(R.styleable.EasyTitleBar_Easy_title)
            if (null != title) {
                title_tv!!.text = title
            } else {
                if (context is Activity) {
                    val pm = context.getPackageManager()
                    try {
                        val activityInfo = pm.getActivityInfo(context.componentName, 0)
                        setTitle(activityInfo.loadLabel(pm).toString())
                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
            titleTextSize = ta.getDimension(R.styleable.EasyTitleBar_Easy_titleSize, titleTextSize)
            title_tv!!.textSize = EasyUtil.px2sp(context, titleTextSize).toFloat()
            titleColor = ta.getColor(R.styleable.EasyTitleBar_Easy_titleColor, titleColor)
            title_tv!!.setTextColor(titleColor)
            lineHeight = ta.getDimension(R.styleable.EasyTitleBar_Easy_lineHeight, lineHeight)
            lineColor = ta.getColor(R.styleable.EasyTitleBar_Easy_lineColor, lineColor)
            val lineParams: androidx.constraintlayout.widget.ConstraintLayout.LayoutParams =
                titleLine!!.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
            lineParams.height = lineHeight.toInt()
            titleLine!!.setBackgroundColor(lineColor)
            titleLine!!.layoutParams = lineParams


            //菜单图标大小
            menuImgSize = ta.getDimension(R.styleable.EasyTitleBar_Easy_menuImgSize, menuImgSize)
            //菜单文字大小
            menuTextSize = ta.getDimension(R.styleable.EasyTitleBar_Easy_menuTextSize, menuTextSize)
            //菜单文字颜色
            menuTextColor = ta.getColor(R.styleable.EasyTitleBar_Easy_menuTextColor, menuTextColor)

            //左边xml添加View
            val leftImageState = ta.getInt(R.styleable.EasyTitleBar_Easy_leftLayoutState, 1)
            if (leftImageState == 1) {
                leftLayout!!.visibility = View.VISIBLE
            } else {
                leftLayout!!.visibility = View.GONE
            }

            //One
            val leftOneText = ta.getString(R.styleable.EasyTitleBar_Easy_leftOneText)
            if (!TextUtils.isEmpty(leftOneText)) {
                addLeftView(
                    MenuBuilder(getContext(), this)
                        .text(leftOneText)
                        .createText()
                )
            }
            val leftOneImage = ta.getDrawable(R.styleable.EasyTitleBar_Easy_leftOneImage)
            if (leftOneImage != null) {
                addLeftView(
                    MenuBuilder(getContext(), this)
                        .drawable(leftOneImage)
                        .createImage()
                )
            }
            //Two
            val leftTwoText = ta.getString(R.styleable.EasyTitleBar_Easy_leftTwoText)
            if (!TextUtils.isEmpty(leftTwoText)) {
                addLeftView(
                    MenuBuilder(getContext(), this)
                        .text(leftTwoText)
                        .createText()
                )
            }
            val leftTwoImage = ta.getDrawable(R.styleable.EasyTitleBar_Easy_leftTwoImage)
            if (leftTwoImage != null) {
                addLeftView(
                    MenuBuilder(getContext(), this)
                        .drawable(leftTwoImage)
                        .createImage()
                )
            }
            //Three
            val leftThreeText = ta.getString(R.styleable.EasyTitleBar_Easy_leftThreeText)
            if (!TextUtils.isEmpty(leftThreeText)) {
                addLeftView(
                    MenuBuilder(getContext(), this)
                        .text(leftThreeText)
                        .createText()
                )
            }
            val leftThreeImage = ta.getDrawable(R.styleable.EasyTitleBar_Easy_leftThreeImage)
            if (leftThreeImage != null) {
                addLeftView(
                    MenuBuilder(getContext(), this)
                        .drawable(leftThreeImage)
                        .createImage()
                )
            }

            //右侧xml添加View
            val rightImageState = ta.getInt(R.styleable.EasyTitleBar_Easy_rightLayoutState, 1)
            if (rightImageState == 1) {
                rightLayout!!.visibility = View.VISIBLE
            } else {
                rightLayout!!.visibility = View.GONE
            }


            //One
            val rightOneText = ta.getString(R.styleable.EasyTitleBar_Easy_rightOneText)
            if (!TextUtils.isEmpty(rightOneText)) {
                addRightView(
                    MenuBuilder(getContext(), this)
                        .text(rightOneText)
                        .createText()
                )
            }
            val rightOneImage = ta.getDrawable(R.styleable.EasyTitleBar_Easy_rightOneImage)
            if (rightOneImage != null) {
                addRightView(
                    MenuBuilder(getContext(), this)
                        .drawable(rightOneImage)
                        .createImage()
                )
            }
            //Two
            val rightTwoText = ta.getString(R.styleable.EasyTitleBar_Easy_rightTwoText)
            if (!TextUtils.isEmpty(rightTwoText)) {
                addRightView(
                    MenuBuilder(getContext(), this)
                        .text(rightTwoText)
                        .createText()
                )
            }
            val rightTwoImage = ta.getDrawable(R.styleable.EasyTitleBar_Easy_rightTwoImage)
            if (rightTwoImage != null) {
                addRightView(
                    MenuBuilder(getContext(), this)
                        .drawable(rightTwoImage)
                        .createImage()
                )
            }
            //Three
            val rightThreeText = ta.getString(R.styleable.EasyTitleBar_Easy_rightThreeText)
            if (!TextUtils.isEmpty(rightThreeText)) {
                addRightView(
                    MenuBuilder(getContext(), this)
                        .text(rightThreeText)
                        .createText()
                )
            }
            val rightThreeImage = ta.getDrawable(R.styleable.EasyTitleBar_Easy_rightThreeImage)
            if (rightThreeImage != null) {
                addRightView(
                    MenuBuilder(getContext(), this)
                        .drawable(rightThreeImage)
                        .createImage()
                )
            }

            //放在titleStyle之前
            viewPadding = ta.getDimension(R.styleable.EasyTitleBar_Easy_viewPadding, viewPadding)
            parentPadding =
                ta.getDimension(R.styleable.EasyTitleBar_Easy_parentPadding, parentPadding)
            val backLayoutParams: androidx.constraintlayout.widget.ConstraintLayout.LayoutParams =
                backLayout!!.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
            backLayoutParams.width = (backImageSize + parentPadding * 2).toInt()
            backLayout!!.layoutParams = backLayoutParams
            leftLayout!!.setPadding((parentPadding - viewPadding / 2).toInt(), 0, 0, 0)
            rightLayout!!.setPadding(0, 0, (parentPadding - viewPadding / 2).toInt(), 0)
            backLayoutState = ta.getInt(R.styleable.EasyTitleBar_Easy_backLayoutState, 1)

            //分割线
            lineState = ta.getInt(R.styleable.EasyTitleBar_Easy_lineState, lineState)
            if (backLayoutState == 1) {
                backImage!!.visibility = View.VISIBLE
                backLayout!!.visibility = View.VISIBLE
            } else {
                backImage!!.visibility = View.GONE
                backLayout!!.visibility = View.GONE
            }
            if (lineState == 1) {
                titleLine!!.visibility = View.VISIBLE
            } else {
                titleLine!!.visibility = View.GONE
            }

//3
            titleStyle = ta.getInt(R.styleable.EasyTitleBar_Easy_titleStyle, titleStyle)
            if (titleStyle == 0) {
                setTitleStyle(TITLE_STYLE_CENTER)
            } else {
                setTitleStyle(TITLE_STYLE_LEFT)
            }
            ta.recycle()
        }
    }


    fun setOnDoubleClickListener(onDoubleClickListener: OnDoubleClickListener?) {
        this.onDoubleClickListener = onDoubleClickListener
    }

    fun setEasyFitsWindows(fitSystemWindow: Boolean) {
        this.fitSystemWindow = fitSystemWindow
        fit_cl.setFitsSystemWindows(fitSystemWindow)
    }

    fun setHasStatusPadding(hasStatusPadding: Boolean) {
        this.hasStatusPadding = hasStatusPadding
        if (hasStatusPadding) {
            val statusParams = status_view!!.layoutParams as LinearLayout.LayoutParams
            statusParams.height = EasyUtil.getStateBarHeight(getContext())
            status_view!!.layoutParams = statusParams
        } else {
            val statusParams = status_view!!.layoutParams as LinearLayout.LayoutParams
            statusParams.height = 0
            status_view!!.layoutParams = statusParams
        }
    }


    fun setFitColor(fitColor: Int) {
        this.fitColor = fitColor
        status_view!!.setBackgroundColor(fitColor)
    }

    override fun setBackgroundColor(color: Int) {
        titleLayout!!.setBackgroundColor(color)
    }

    fun getLeftLayout(): ViewGroup? {
        return leftLayout
    }

    fun getRightLayout(): ViewGroup? {
        return rightLayout
    }

    fun getMenuImgSize(): Float {
        return menuImgSize
    }

    fun getMenuTextSize(): Float {
        return menuTextSize
    }

    fun getMenuTextColor(): Int {
        return menuTextColor
    }

    /**
     * 返回箭头的父布局
     *
     * @return
     */
    fun getBackLayout(): LinearLayout? {
        return backLayout
    }

    fun getTitleStyle(): Int {
        return titleStyle
    }

    /**
     * 获取标题
     *
     * @return
     */
    fun getTitle(): String? {
        return title
    }

    /**
     * 获取标题View
     *
     * @return
     */
    fun getTitleView(): TextView? {
        return title_tv
    }

    /**
     * 设置标题文字
     */
    fun setTitle(title: CharSequence?) {
        title_tv!!.text = title
    }

    /**
     * 设置标题字体大小
     */
    fun setTitleSize(textSize: Float) {
        title_tv!!.textSize = textSize
    }

    /**
     * 设置标题字体颜色
     */
    fun setTitleColor(textColor: Int) {
        title_tv!!.setTextColor(textColor)
    }

    /**
     * 设置标题排列方式（一种居中、一种靠左）
     *
     * @param style
     */
    @SuppressLint("NewApi")
    fun setTitleStyle(style: Int) {
        titleStyle = style
        backLayoutState =
            if (backLayout!!.visibility == View.VISIBLE && backImage!!.visibility == View.VISIBLE) {
                1
            } else {
                0
            }
        lineState = if (titleLine!!.visibility == View.VISIBLE) {
            1
        } else {
            0
        }
        if (style == TITLE_STYLE_CENTER) {
            centerConstraintSet.connect(
                title_tv!!.id,
                ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.LEFT,
                0
            )
            centerConstraintSet.connect(
                title_tv!!.id,
                ConstraintSet.RIGHT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.RIGHT,
                0
            )
            centerConstraintSet.connect(
                title_tv!!.id,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
            )
            centerConstraintSet.connect(
                title_tv!!.id,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                0
            )
            centerConstraintSet.applyTo(fit_cl)
        } else if (style == TITLE_STYLE_LEFT) {
            leftConstraintSet.connect(
                title_tv!!.id,
                ConstraintSet.LEFT,
                backLayout!!.id,
                ConstraintSet.RIGHT,
                0
            )
            leftConstraintSet.setMargin(title_tv!!.id, ConstraintSet.LEFT, 0)
            leftConstraintSet.setGoneMargin(
                title_tv!!.id,
                ConstraintSet.LEFT,
                parentPadding.toInt()
            )
            leftConstraintSet.connect(
                title_tv!!.id,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
            )
            leftConstraintSet.connect(
                title_tv!!.id,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                0
            )
            leftConstraintSet.applyTo(fit_cl)
        }
        val backLayoutParams: androidx.constraintlayout.widget.ConstraintLayout.LayoutParams =
            backLayout!!.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
        backLayoutParams.width = (backImageSize + parentPadding * 2).toInt()
        backLayout!!.layoutParams = backLayoutParams
        if (backLayoutState == 1) {
            backImage!!.visibility = View.VISIBLE
            backLayout!!.visibility = View.VISIBLE
        } else {
            backImage!!.visibility = View.GONE
            backLayout!!.visibility = View.GONE
        }
        if (lineState == 1) {
            titleLine!!.visibility = View.VISIBLE
        } else {
            titleLine!!.visibility = View.GONE
        }
    }


    /**
     * 获取标题分割线
     *
     * @return
     */
    fun getTitleLine(): View? {
        return titleLine
    }

    fun attachScrollView(
        view: View?,
        color: Int,
        height: Int,
        onSrollAlphaListener: OnSrollAlphaListener?
    ) {
        if (view != null) {
            EasyUtil.addOnSrollListener(view, object : EasyUtil.OnSrollListener {
                override fun onSrollEvent(scrollY: Int) {
                    val baseColor: Int = getResources().getColor(color)
                    val alpha = Math.min(1f, scrollY.toFloat() / height)
                    setBackgroundColor(EasyUtil.getColorWithAlpha(alpha, baseColor))
                    onSrollAlphaListener?.OnSrollAlphaEvent(alpha)
                }
            })
        }
    }


    //双击事件
    interface OnDoubleClickListener {
        fun onDoubleEvent(view: View?)
    }

    interface OnSrollAlphaListener {
        fun OnSrollAlphaEvent(alpha: Float)
    }


    fun dip2px(dpValue: Float): Int {
        val scale: Float = getContext().getResources().getDisplayMetrics().density
        return (dpValue * scale + 0.5f).toInt()
    }



    fun getRightLayout(position: Int): View? {
        return rightLayout!!.getChildAt(rightLayout!!.childCount - 1 - position)
    }

    fun getLeftLayout(position: Int): View? {
        return leftLayout!!.getChildAt(position)
    }


    class MenuBuilder(private val context: Context, titleBar: EasyTitleBar) {
        private var onMenuClickListener: OnMenuClickListener? = null
        private var text: String? = null
        private var icon = 0
        private var drawable: Drawable? = null
        private var paddingleft: Int
        private var paddingright: Int
        private var menuImgSize: Float
        private var menuTextSize: Float
        private var menuTextColor: Int

        init {
            paddingleft = (titleBar.viewPadding / 2).toInt()
            paddingright = (titleBar.viewPadding / 2).toInt()
            menuImgSize = titleBar.menuImgSize
            menuTextSize = EasyUtil.px2sp(context, titleBar.menuTextSize).toFloat()
            menuTextColor = titleBar.menuTextColor
        }

        fun text(text: String?): MenuBuilder {
            this.text = text
            return this
        }

        fun menuTextSize(menuTextSize: Float): MenuBuilder {
            this.menuTextSize = menuTextSize
            return this
        }

        fun listener(onMenuClickListener: OnMenuClickListener?): MenuBuilder {
            this.onMenuClickListener = onMenuClickListener
            return this
        }

        fun paddingleft(paddingleft: Int): MenuBuilder {
            this.paddingleft = paddingleft
            return this
        }

        fun paddingright(paddingright: Int): MenuBuilder {
            this.paddingright = paddingright
            return this
        }

        fun icon(icon: Int): MenuBuilder {
            this.icon = icon
            return this
        }

        fun menuImgSize(menuImgSize: Int): MenuBuilder {
            this.menuImgSize = menuImgSize.toFloat()
            return this
        }

        fun drawable(drawable: Drawable?): MenuBuilder {
            this.drawable = drawable
            return this
        }

        fun menuTextColor(menuTextColor: Int): MenuBuilder {
            this.menuTextColor = menuTextColor
            return this
        }

        fun menuTextSize(menuTextSize: Int): MenuBuilder {
            this.menuTextSize = menuTextSize.toFloat()
            return this
        }

        fun onItemClickListener(onMenuClickListener: OnMenuClickListener?): MenuBuilder {
            this.onMenuClickListener = onMenuClickListener
            return this
        }

        fun createText(): View {
            val textView = TextView(context)
            textView.text = text
            textView.textSize = menuTextSize
            textView.setTextColor(menuTextColor)
            textView.setPadding(paddingleft, 0, paddingright, 0)
            textView.gravity = Gravity.CENTER
            val textParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            textView.layoutParams = textParams
            textView.setOnClickListener { if (onMenuClickListener != null) onMenuClickListener!!.OnMenuEvent() }
            return textView
        }

        fun createImage(): View {
            val imageView = ImageView(context)
            if (drawable != null) imageView.setImageDrawable(drawable) else if (icon != 0) {
                imageView.setImageResource(icon)
            } else {
                imageView.setImageBitmap(null)
            }
            val imageParams: LinearLayout.LayoutParams
            if (menuImgSize < 0) {
                imageParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            } else {
                imageParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                imageParams.width = (menuImgSize + paddingleft + paddingright).toInt()
            }
            imageView.layoutParams = imageParams
            imageView.setPadding(paddingleft, 0, paddingright, 0)
            imageView.setOnClickListener { if (onMenuClickListener != null) onMenuClickListener!!.OnMenuEvent() }
            return imageView
        }

        interface OnMenuClickListener {
            fun OnMenuEvent()
        }
    }

    fun addRightView(view: View?) {
        rightLayout!!.addView(view, 0)
    }

    fun addLeftView(view: View?) {
        leftLayout!!.addView(view)
    }

    fun addRightImg(res: Int): ImageView? {
        return addRightImg(res, null)
    }

    fun addRightText(str: String?): TextView? {
        return addRightText(str, null)
    }

    fun addLeftImg(res: Int): ImageView? {
        return addLeftImg(res, null)
    }

    fun addLeftText(str: String?): TextView? {
        return addLeftText(str, null)
    }

    fun addRightImg(res: Int, onMenuClickListener: MenuBuilder.OnMenuClickListener?): ImageView? {
        val imageView = MenuBuilder(getContext(), this)
            .icon(res)
            .listener(onMenuClickListener)
            .createImage() as ImageView
        addRightView(imageView)
        return imageView
    }

    fun addRightText(str: String?, onMenuClickListener: MenuBuilder.OnMenuClickListener?): TextView? {
        val textView = MenuBuilder(getContext(), this)
            .text(str)
            .listener(onMenuClickListener)
            .createText() as TextView
        addRightView(textView)
        return textView
    }

    fun addLeftImg(res: Int, onMenuClickListener: MenuBuilder.OnMenuClickListener?): ImageView? {
        val imageView = MenuBuilder(getContext(), this)
            .icon(res)
            .listener(onMenuClickListener)
            .createImage() as ImageView
        addLeftView(imageView)
        return imageView
    }

    fun addLeftText(str: String?, onMenuClickListener: MenuBuilder.OnMenuClickListener?): TextView? {
        val textView = MenuBuilder(getContext(), this)
            .text(str)
            .listener(onMenuClickListener)
            .createText() as TextView
        addLeftView(textView)
        return textView
    }
}