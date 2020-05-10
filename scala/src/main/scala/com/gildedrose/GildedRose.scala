package com.gildedrose

import scala.util.matching.Regex

class GildedRose {
  val BackstagePassesPattern: Regex = "(\\bBackstage passes\\b).*".r
  val SulfurasPattern: Regex = "(\\bSulfuras\\b).*".r
  val AgedBriePattern: Regex = "(\\bAged Brie\\b).*".r

  def calcSellIn(item: Item): Int = (item.name, item.sellIn) match {
    case (SulfurasPattern(_*), sI) => sI
    case (_, sI)                   => sI - 1
  }

  def updateQuality(items: List[Item]): List[Item] = {
    items
      .map(item => item.copy(sellIn = calcSellIn(item)))
      .map(item => item.copy(quality = calcQuality(item)))
      .map(item => item.copy(quality = fitQualityInBounds(item)))
  }

  private def fitQualityInBounds(item: Item) = (item.name, item.quality) match {
    case (SulfurasPattern(_*), q) => q
    case (_, q) => Math.min(50, Math.max(0, q))
  }

  private def calcQuality(item: Item) = {
    (item.name, item.sellIn) match {
      case (BackstagePassesPattern(_*), sI) =>
        if (sI < 0) 0
        else if (sI < 5) item.quality + 3
        else if (sI < 10) item.quality + 2
        else item.quality + 1

      case (SulfurasPattern(_*), _) => item.quality

      case (AgedBriePattern(_*), sI) => item.quality + changeQuality(sI) * -1

      case (_, sI) => item.quality + changeQuality(sI)
    }
  }

  private val defaultDiff = -1
  private def changeQuality(sI: Int) =
    if (sI >= 0) defaultDiff else defaultDiff * 2

}
