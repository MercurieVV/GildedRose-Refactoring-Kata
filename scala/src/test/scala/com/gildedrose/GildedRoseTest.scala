package com.gildedrose

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GildedRoseTest  extends AnyFlatSpec with Matchers {
      it should "foo" in {
        val items = List[Item](new Item("foo", 0, 0))
        val app = new GildedRose()
        val updatedItems = app.updateQuality(items)
        updatedItems(0).name should equal ("fixme")
      }
}