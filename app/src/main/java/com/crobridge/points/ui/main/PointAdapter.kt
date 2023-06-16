/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.crobridge.points

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crobridge.points.db.Point
import com.crobridge.points.databinding.ListItemPointBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class PointAdapter(val clickListener: PointListener) : ListAdapter<DataItem,
        RecyclerView.ViewHolder>(PointDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<Point>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.HeaderItem)
                else -> listOf(DataItem.HeaderItem) + list.map { DataItem.PointItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PointViewHolder -> {
                val pointItem = getItem(position) as DataItem.PointItem
                holder.bind(clickListener, pointItem.point)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> PointViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return HeaderViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.HeaderItem -> ITEM_VIEW_TYPE_HEADER
            is DataItem.PointItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class PointViewHolder private constructor(val binding: ListItemPointBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: PointListener, item: Point) {
            binding.point = item
            binding.clickListener = clickListener
            binding.executePendingBindings() // needed when using @BindingAdapters
        }

        companion object {
            fun from(parent: ViewGroup): PointViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPointBinding.inflate(layoutInflater, parent, false)
                return PointViewHolder(binding)
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class PointDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class PointListener(val clickListener: (id: Long) -> Unit) {
    fun onClick(p: Point) = clickListener(p.id)
}


// because we have header as item in List
sealed class DataItem {
    data class PointItem(val point: Point): DataItem() {
        override val id = point.id
    }

    object HeaderItem: DataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long // to be overriden by both implementations
}
