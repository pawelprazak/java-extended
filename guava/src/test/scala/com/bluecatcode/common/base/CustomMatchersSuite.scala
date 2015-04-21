package com.bluecatcode.common.base

import com.bluecatcode.common.base.CustomMatchers._
import org.junit.runner.RunWith
import org.scalatest.Matchers._
import org.scalatest.exceptions.TableDrivenPropertyCheckFailedException
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.PropertyChecks
import org.scalatest.{FunSuite, GivenWhenThen}

@RunWith(classOf[JUnitRunner])
class CustomMatchersSuite extends FunSuite with GivenWhenThen with PropertyChecks {

  test("Test 'firstUpper' matcher") {
    Given("an invalid string")
    val string = "value"
    val expected = "Value"

    When("is applied to matcher")
    val result = firstCharUpper.apply(string)

    Then("result should be false")
    result.actualValue should be(string.head)
    result.expectedValue should be(expected.head)
    result.matches should equal(false)
  }

  test("Test 'theSameTailAs' matcher pass") {
    val samples = Table(("left", "right"),
      ("aaa", "baa"),
      ("bing", "ding")
    )

    forAll(samples) {
      (l, r) => l should have(theSameTailAs(r))
    }
  }

  test("Test 'theSameTailAs' matcher failure") {
    val samples = Table(("left", "right"),
      ("aaa", "bbb"),
      ("bing", "lol")
    )

    intercept[TableDrivenPropertyCheckFailedException] {
      forAll(samples) {
        (l, r) => l should have(theSameTailAs(r))
      }
    }
  }

}
