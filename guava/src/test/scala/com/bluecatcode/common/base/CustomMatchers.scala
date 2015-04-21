package com.bluecatcode.common.base

import org.scalatest.matchers.{HavePropertyMatchResult, HavePropertyMatcher}

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

object CustomMatchers extends CustomMatchers
