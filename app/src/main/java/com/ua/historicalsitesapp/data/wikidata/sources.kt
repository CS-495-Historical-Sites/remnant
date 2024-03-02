package com.ua.historicalsitesapp.data.wikidata

fun constructWikidataImageLink(
    imageName: String,
    width: Int,
): String {
  return "https://commons.wikimedia.org/w/index.php?title=Special:Redirect/file/$imageName&width=$width"
}
