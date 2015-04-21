package com.bluecatcode.common.base

import com.bluecatcode.common.base.Strings._
import org.junit.runner.RunWith
import org.scalacheck.Gen
import org.scalacheck.Gen._
import org.scalatest.Matchers._
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.{HavePropertyMatchResult, HavePropertyMatcher}
import org.scalatest.prop.PropertyChecks
import org.scalatest.{FunSuite, _}

@RunWith(classOf[JUnitRunner])
class StringsSuite extends FunSuite with GivenWhenThen with PropertyChecks with CustomMatchers {

  val nonEmptyAlphaStr: Gen[String] = nonEmptyListOf(alphaChar).map(_.mkString).suchThat(_.forall(_.isLetter))

  test("Should capitalize any alphanumeric string") {
    Given("an alphabetic string")
    forAll(nonEmptyAlphaStr) { (s: String) =>
      When(s"the string is not empty, got: $s")
      val result = capitalize(s)

      Then(s"result's first character is upper case, got: $result")
      result should have(firstCharUpper)
      result should have(theSameTailAs(s))
      result shouldNot be(empty)
    }
  }

  test("Should leave any non-letter and already capitalized starting letter") {
    val samples = Table(
      "string", // header
      "", " ", "\n", "\t", "\r", "0", "中国", "TLA"
    )
    forAll(samples) {
      s => capitalize(s) should be(s)
    }
  }

  test("Should capitalize any first lower case letter") {
    val samples = Table(
      ("string", "expected"), // header
      ("a", "A"),
      ("łąka", "Łąka")
    )
    forAll(samples) {
      (s, e: String) => capitalize(s) should be(e)
    }
  }

  test("Test 'firstUpper' custom matcher") {
    Given("an invalid string")
    val string = "value"
    val expected = "Value"

    When("is applied to matcher")
    val result = firstCharUpper.apply(string)

    Then("result should be false")
    result.actualValue should be(string.head)
    result.expectedValue should be(expected.head)
    result.matches should be(false)
  }
}

trait CustomMatchers {

  def firstCharUpper =
    new HavePropertyMatcher[String, Character] {
      override def apply(string: String) =
        HavePropertyMatchResult(
          string.head.isUpper,
          "first character is upper",
          string.head.toUpper,
          string.head
        )
    }

  def theSameTailAs(right: String) =
    new HavePropertyMatcher[String, String] {
      override def apply(left: String) =
        HavePropertyMatchResult(
          left.tail.equals(right.tail),
          "tails are the same",
          right.tail,
          left.tail
        )
    }
}