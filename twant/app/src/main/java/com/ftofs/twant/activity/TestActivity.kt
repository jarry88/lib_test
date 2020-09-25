package com.ftofs.twant.activity

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ftofs.twant.R
import com.ftofs.twant.dsl.*
import com.ftofs.twant.fragment.LabFragment
import com.ftofs.twant.hot_zone.HotView
import com.gzp.lib_common.base.BaseActivity

class TestActivity : AppCompatActivity() {

    private val contentView by lazy{
        ConstraintLayout {
            layout_width = match_parent
            layout_height= match_parent
            TextView {
                layout_width= wrap_content
                layout_height= wrap_content
                text="commit"
                textSize=30f
                padding=10
                shape {
                    shape=GradientDrawable.RECTANGLE
                    cornerRadius=10f

                }
                top_toTopOf= parent_id

            }
            HotView{
                layout_width= match_parent
                layout_height= wrap_content
                bottom_toBottomOf= parent_id
            }
            FrameLayout {
                layout_width= match_parent
                layout_height= match_parent
                
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentView)
    }
}