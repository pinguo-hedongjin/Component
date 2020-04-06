package com.kubi.sdk.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.kubi.sdk.R

/**
 * author:  hedongjin
 * date:  2019-10-11
 * description: Please contact me if you have any questions
 */
class TitleBar(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : RelativeLayout(context, attrs, defStyleAttr) {

    private lateinit var backView: ImageView
    private lateinit var titleView: TextView
    private var titleLayout: FrameLayout? = null
    private var menuLayout: LinearLayout? = null

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)
        initNavigation(typedArray)
        initTitle(typedArray)
        typedArray.recycle()

    }

    fun setNavigationIcon(icon: Int) {
        backView.setImageResource(icon)
    }

    fun setOnNavigationListener(listener: View.OnClickListener) {
        backView.setOnClickListener(listener)
    }

    fun setTitle(title: String? = null) {
        titleView.text = title
        titleView.visibility = View.VISIBLE
        titleLayout?.visibility = View.GONE
    }

    fun setTitle(view: View? = null, layoutParams: FrameLayout.LayoutParams =  FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)) {
        val layout = titleLayout ?: FrameLayout(context).also {
            addView(it, LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                addRule(CENTER_IN_PARENT)
            })
            titleLayout = it
        }
        layout.removeAllViews()

        view?.let {
            layout.addView(it, layoutParams)
        }

        titleView.visibility = View.GONE
        layout.visibility = View.VISIBLE
    }

    fun setMenu(vararg list: Menu) {
        if (list.isEmpty()) {
            return
        }

        val layout = menuLayout ?: LinearLayout(context).also {
            it.orientation = LinearLayout.HORIZONTAL
            addView(it, LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {
                        addRule(CENTER_VERTICAL)
                        addRule(ALIGN_PARENT_RIGHT)
            })
            menuLayout = it
        }
        layout.removeAllViews()

        list.forEachIndexed { index, menu ->
            val child = AppCompatTextView(context)
            child.gravity = Gravity.CENTER
            child.text = menu.text?.let { resources.getString(it) }
            child.setTextSize(TypedValue.COMPLEX_UNIT_SP, menu.textSize.toFloat())
            child.setTextColor(resources.getColor(menu.textColor))
            child.setPadding(dp2px(context, 12), 0, dp2px(context, 12), 0)
            child.setImageResource(menu.icon, menu.direction)
            child.setOnClickListener(menu.listener)

            layout.addView(child, LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT))
        }
    }

    fun show() {
        visibility = View.VISIBLE
    }

    fun hide() {
        visibility = View.GONE
    }

    private fun initNavigation(typedArray: TypedArray) {
        backView = AppCompatImageView(context)
        backView.scaleType = ImageView.ScaleType.CENTER
        backView.setImageDrawable(
                typedArray.getDrawable(R.styleable.TitleBar_navigationIcon)
                        ?: context.resources.getDrawable(R.mipmap.ic_back)
        )
        addView(backView, LayoutParams(dp2px(context, 33), ViewGroup.LayoutParams.MATCH_PARENT))

    }

    private fun initTitle(typedArray: TypedArray) {
        titleView = AppCompatTextView(context)
        titleView.gravity = Gravity.CENTER
        titleView.text = typedArray.getText(R.styleable.TitleBar_titleText)
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.TitleBar_titleSize, dp2px(context, 18)).toFloat())
        titleView.setTextColor(typedArray.getColor(R.styleable.TitleBar_titleColor, resources.getColor(R.color.font_main_dark_white)))

        addView(
                titleView,
                LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    addRule(CENTER_IN_PARENT)
                }
        )

    }

    private fun TextView.setImageResource(icon: Int?, direction: Int = Gravity.LEFT) {
        val drawable: ((Int?) -> Drawable?) = { it?.let { context.resources.getDrawable(it) } }
        when (direction) {
            Gravity.LEFT, Gravity.START -> setCompoundDrawables(drawable(icon), null, null, null)
            Gravity.RIGHT, Gravity.END -> setCompoundDrawables(null, null, drawable(icon), null)
            Gravity.TOP -> setCompoundDrawables(null, drawable(icon), null, null)
            Gravity.BOTTOM -> setCompoundDrawables(null, null, null, drawable(icon))
        }
    }



    companion object {
        fun dp2px(context: Context, dp: Int): Int {
            val scale = context.resources.displayMetrics.density
            return (dp * scale + 0.5f).toInt()
        }
    }
}

data class Menu(
        @DrawableRes val icon: Int? = null,
        val direction: Int = Gravity.LEFT,
        @StringRes val text: Int? = null,
        @ColorRes val textColor: Int = R.color.font_tips_light_gray_blue,
        val textSize: Int = 16,
        val listener: View.OnClickListener? = null
)