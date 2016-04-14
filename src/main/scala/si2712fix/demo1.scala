package si2712fix

object Demo1a {
  def meh[M[_], A](x: M[A]): M[A] = x

  meh((x: Int) => x) // solvex ?M = [X](Int => X) and ?A = Int ...
}

object Demo1b {
  class Foo[T, F[_]]

  def meh[M[_[_]], F[_]](x: M[F]): M[F] = x

  meh(new Foo[Int, List]) // solves ?M = [X[_]]Foo[Int, X[_]] ?F = List ...
}

object Demo1c {
  trait TC[T]
  class Foo[F[_], G[_]]

  def meh[M[_[_]]](x: M[TC]): M[TC] = x

  meh(new Foo[TC, TC]) // solves ?M = [X[_]]Foo[TC, X]
}

object Demo1d {
  trait TC[F[_]]
  trait TC2[F[_]]
  class Foo[F[_[_]], G[_[_]]]
  new Foo[TC, TC2]

  def meh[M[_[_[_]]]](x: M[TC2]): M[TC2] = x

  meh(new Foo[TC, TC2]) // solves ?M = [X[_[_]]]Foo[TC, X]
}
