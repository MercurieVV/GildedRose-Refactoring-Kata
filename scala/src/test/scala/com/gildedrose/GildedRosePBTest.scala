package com.gildedrose

import org.scalacheck._
import org.scalatest.FunSuite
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
    nameRnd <- Gen.alphaNumStr
    nameSel <- Gen.oneOf(List("+5 Dexterity Vest", "Aged Brie", "Elixir of the Mongoose", "Sulfuras, Hand of Ragnaros", "Backstage passes to a TAFKAL80ETC concert"))
    name <- Gen.oneOf(nameRnd, nameSel)
    sellIn <- Gen.choose(-100, 100)
    quality <- Gen.choose(0, 50)
  } yield new Item(name, sellIn, quality)
  val itemsGen: Gen[List[Item]] = Gen.listOf(itemGen)

  test("all conditions"){
    forAll(itemsGen){ items: List[Item] =>
      val app = new GildedRose(items.toArray)
      app.updateQuality()
      items.foreach(item => {
        assert(item.quality >= 0)
        assert(item.quality <= 50)
      })
    }
  }
}
