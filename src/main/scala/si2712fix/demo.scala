package si2712fix

import scala.language.higherKinds

object Test {
  def meh[M[_], A](x: M[A]): M[A] = x
  meh{(x: Int) => x} // solvex ?M = [X] Int => X and ?A = Int ...
}
