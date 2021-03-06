package com.gildedrose

import org.scalacheck._
import org.scalatest.{Assertion, FunSuite}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

/**
  * Created with IntelliJ IDEA.
  * User: Victor Mercurievv
  * Date: 5/10/2020
  * Time: 1:57 AM
  * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
  */
class GildedRosePBTest extends FunSuite with ScalaCheckPropertyChecks {
  val itemGen = for {
    name <- Gen.oneOf(
      List(
        "+5 Dexterity Vest",
        "Aged Brie",
        "Elixir of the Mongoose",
        "Sulfuras, Hand of Ragnaros",
        "Backstage passes to a TAFKAL80ETC concert",
        "Conjured",
        "ololo unknown name"
      )
    )
    sellIn <- Gen.choose(-100, 100)
    quality <- Gen.choose(-10, 60)
  } yield Item(name, sellIn, quality)
  val itemsGen: Gen[List[Item]] = Gen.listOf(itemGen)

  test("quality value bound conditions") {
    forAll(itemsGen) { items: List[Item] =>
      val app = new GildedRose()
      val updatedItems = app.updateQuality(items)
      updatedItems.foreach(item => {
        assert(item.quality >= 0)
        assert(item.quality <= 50)
      })
    }
  }
  test("test changed props") {
    forAll(itemGen, MinSuccessful(10000)) { itemBefore: Item =>
      val app = new GildedRose()
      val itemAfter = app.updateQuality(List(itemBefore))(0)


      val BackstagePassesPattern = "(\\bBackstage passes\\b).*".r
      val SulfurasPattern = "(\\bSulfuras\\b).*".r
      val AgedBriePattern = "(\\bAged Brie\\b).*".r
      val ConjuredPattern = "(\\bConjured\\b).*".r
      val expectedQualityWithDiff: Int => Int = expectedQualityWithinBounds(itemBefore.quality)


      (itemAfter.name, itemAfter.sellIn) match {
        case (BackstagePassesPattern(_*), sI) =>
          if (sI < 0) assert(itemAfter.quality == 0)
          else if (sI < 5) assert(itemAfter.quality == expectedQualityWithDiff(3))
          else if (sI < 10) assert(itemAfter.quality == expectedQualityWithDiff(2))
          else assert(itemAfter.quality == expectedQualityWithDiff(1))

        case (SulfurasPattern(_*), _) => assert(itemAfter.quality == itemBefore.quality)

        case (AgedBriePattern(_*), sI) if (sI < 0) => assert(itemAfter.quality == expectedQualityWithDiff(2))
        case (AgedBriePattern(_*), sI)  if (sI >= 0) => assert(itemAfter.quality == expectedQualityWithDiff(1))

        case (ConjuredPattern(_*), sI)  if (sI < 0) => assert(itemAfter.quality == expectedQualityWithDiff(-4))
        case (ConjuredPattern(_*), sI)  if (sI >= 0) => assert(itemAfter.quality == expectedQualityWithDiff(-2))

        case (_, sI) if (sI < 0)      => assert(itemAfter.quality == expectedQualityWithDiff(-2))
        case (_, sI) if (sI >= 0)     => assert(itemAfter.quality == expectedQualityWithDiff(-1))
      }

      itemAfter.name match {
        case SulfurasPattern(_*) => assert(itemAfter.sellIn == itemBefore.sellIn)
        case _ => assert(itemAfter.sellIn == itemBefore.sellIn - 1)
      }
    }
  }

  private def expectedQualityWithinBounds(oldQuality: Int)(expectedDiff: Int) = {
    Math.max(0, Math.min(50, oldQuality + expectedDiff))
  }

}
