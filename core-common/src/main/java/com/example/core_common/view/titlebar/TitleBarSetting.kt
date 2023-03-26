package com.example.core_common.view.titlebar

import android.graphics.Color
import com.example.core_common.R

class TitleBarSetting {
    private lateinit var setting: TitleBarSetting

    //默认返回图标
    private var back_icon: Int = R.drawable.back_black

    //默认背景颜色
    private var backgroud = Color.parseColor("#62e3ec")

    //默认标题大小
    private var titleSize = 18

    //默认标题颜色
    private var titleColor = Color.parseColor("#ffffff")

    //标题栏分割线的有无
    private var lineShow = true

    //默认标题栏高度
    private var titleBarHeight = 48
    private var parentPadding = 15
    private var viewPadding = 20

    //返回图片的大小
    private var backImageSize = 18

    //menu图片大小
    private var menuImgSize = 18

    //menu文字大小
    private var menuTextSize = 16

    //menu文字颜色
    private var menuTextColor = Color.parseColor("#ffffff")
    private var titleStyle = 0

    private var lineHeight = 1
    private var lineColor = Color.parseColor("#f7f7f7")
    private var fitSystemWindow = false
    private var hasStatusPadding = false

    fun getInstance(): TitleBarSetting {
        if (setting == null) {
            setting = TitleBarSetting()
        }
        return setting
    }

    fun lineColor(lineColor: Int): TitleBarSetting {
        this.lineColor = lineColor
        return this
    }

    fun backIconRes(back_icon: Int): TitleBarSetting {
        this.back_icon = back_icon
        return this
    }

    fun menuTextSize(menuTextSize: Int): TitleBarSetting {
        this.menuTextSize = menuTextSize
        return this
    }

    fun menuTextColor(menuTextColor: Int): TitleBarSetting {
        this.menuTextColor = menuTextColor
        return this
    }

    fun backgroud(backgroud: Int): TitleBarSetting {
        this.backgroud = backgroud
        return this
    }

    fun titleSize(titleSize: Int): TitleBarSetting {
        this.titleSize = titleSize
        return this
    }

    fun titleColor(titleColor: Int): TitleBarSetting {
        this.titleColor = titleColor
        return this
    }

    fun ti(titleColor: Int): TitleBarSetting {
        this.titleColor = titleColor
        return this
    }

    fun showLine(lineShow: Boolean): TitleBarSetting {
        this.lineShow = lineShow
        return this
    }

    fun parentPadding(parentPadding: Int): TitleBarSetting {
        this.parentPadding = parentPadding
        return this
    }

    fun titleBarHeight(height: Int): TitleBarSetting {
        titleBarHeight = height
        return this
    }

    fun backImageSize(size: Int): TitleBarSetting {
        backImageSize = size
        return this
    }

    fun menuImgSize(size: Int): TitleBarSetting {
        menuImgSize = size
        return this
    }

    fun titleStyle(titleStyle: Int): TitleBarSetting {
        this.titleStyle = titleStyle
        return this
    }

    fun lineHeight(lineHeight: Int): TitleBarSetting {
        this.lineHeight = lineHeight
        return this
    }

    fun viewPadding(viewPadding: Int): TitleBarSetting {
        this.viewPadding = viewPadding
        return this
    }

    fun fitSystemWindow(fitSystemWindow: Boolean): TitleBarSetting {
        this.fitSystemWindow = fitSystemWindow
        return this
    }

    fun hasStatusPadding(hasStatusPadding: Boolean): TitleBarSetting {
        this.hasStatusPadding = hasStatusPadding
        return this
    }

    fun getTitleStyle(): Int {
        return titleStyle
    }

    fun getBackImageSize(): Int {
        return backImageSize
    }


    fun getMenuTextSize(): Int {
        return menuTextSize
    }

    fun getMenuTextColor(): Int {
        return menuTextColor
    }

    fun getBack_icon(): Int {
        return back_icon
    }

    fun getBackgroud(): Int {
        return backgroud
    }

    fun getTitleSize(): Int {
        return titleSize
    }

    fun getTitleColor(): Int {
        return titleColor
    }

    fun getShowLine(): Boolean {
        return lineShow
    }

    fun getTitleBarHeight(): Int {
        return titleBarHeight
    }

    fun setTitleBarHeight(height: Int) {
        titleBarHeight = height
    }

    fun getParentPadding(): Int {
        return parentPadding
    }

    fun getViewPadding(): Int {
        return viewPadding
    }


    fun getMenuImgSize(): Int {
        return menuImgSize
    }


    fun getLineHeight(): Int {
        return lineHeight
    }


    fun getLineColor(): Int {
        return lineColor
    }


    fun isFitSystemWindow(): Boolean {
        return fitSystemWindow
    }

    fun isHasStatusPadding(): Boolean {
        return hasStatusPadding
    }
}