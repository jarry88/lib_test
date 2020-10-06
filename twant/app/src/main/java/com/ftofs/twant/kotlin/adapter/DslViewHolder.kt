package com.ftofs.twant.kotlin.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * A generic ViewHolder that works with a [ViewDataBinding].
 * @param <T> The type of the ViewDataBinding.
</T> */
class DslViewHolder constructor(dslView: View) :
    RecyclerView.ViewHolder(dslView)