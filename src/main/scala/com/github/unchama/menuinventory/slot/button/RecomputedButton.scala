package com.github.unchama.menuinventory.slot.button

import cats.effect.IO
import com.github.unchama.menuinventory.slot.button.action.ButtonEffect

object RecomputedButton {
  /**
   * クリックされるたびに[buttonComputation]に基づいてスロット自体が更新される[Button]を作成する.
   */
  def apply(buttonComputation: IO[Button]): IO[Button] =
    buttonComputation.map { computedButton =>
      val recomputation = ButtonEffect { scope => _ =>
        this(buttonComputation).flatMap(scope.overwriteCurrentSlotBy)
      }

      computedButton.withAnotherEffect(recomputation)
    }
}
