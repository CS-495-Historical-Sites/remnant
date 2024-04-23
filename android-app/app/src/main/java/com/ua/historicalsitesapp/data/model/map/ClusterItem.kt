package com.ua.historicalsitesapp.data.model.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class ClusterItem(
    val itemId: Int,
    val itemPosition: LatLng,
    val itemTitle: String,
    val itemSnippet: String,
    val itemZIndex: Float,
    val imageLink: String,
) : ClusterItem {
  override fun getPosition(): LatLng = itemPosition

  override fun getTitle(): String = itemTitle

  override fun getSnippet(): String = itemSnippet

  override fun getZIndex(): Float = itemZIndex
}
