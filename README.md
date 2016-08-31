# Demo project exercising `-Ypartial-unification` (aka the fix for SI-2712)

This repository contains a minimal SBT project for experimenting with `-Ypartial-unification`, the fix for
[SI-2712][si2712], implemented in [Typelevel Scala][tls] 2.11.8. It has been [merged][merged-2.12] in Lightbend Scala
for 2.12.0-RC1 and there is an open [pull request][pr-2.11] for it to be included in Lightbend Scala 2.11.9 &mdash; if
you like what you see and would like it to be merged in Lightbend Scala 2.11.x then please lend your support to the
latter PR.

# Experimenting with this repo

This project contains a minimal `build.sbt` configured to use Typelevel Scala. It includes the with the example from
[the ticket][si2712] and several other examples which would not have compiled prior to the fix. Things to play with
are anything which you would normally expect to have to use a Cats/Scalaz `Unapply` or `U` suffixed method, or
anything where the arity of the type constructor being inferred is lower than the arity of the outer type constructor
of the concrete type being unified with.

# How to trial this fix with your project

To use Typelevel Scala compiler with your own project you should first ensure that it builds correctly with
Scala 2.11.8, then,

+ Update your `project/build.properties` to require SBT 0.13.13-M1 or later,

  ```
  sbt.version=0.13.13-M1
  ```

+ Add the following to your `build.sbt` immediately next to where you set `scalaVersion`,

  ```
  scalaOrganization := "org.typelevel"
  ```

+ Add the Scala compiler option to enable partial unification,

  ```
  scalacOptions += "-Ypartial-unification"
  ```

If your project and its upstream dependencies have never had issues with SI-2712 and it builds with vanilla Scala
2.11.8 then it will build with Typelevel Scala too.

If your project or some of its upstream dependencies have had issues with SI-2712 and have attempted to work around
them, then it is possible that you will see some errors when compiling with Typelevel Scala with
`-Ypartial-unification` enabled. Typically these will be the result of additional implicit definitions which were
introduced to make up for failures in unification becoming newly ambiguous with the baseline definitions. In cases
like these, the fix is to simply delete the additional implicit definitions. An example of this can be seen in the
[Cats branch][catsbuild] mentioned earlier where four now-redundant implicit definitions in the tests must be deleted
to eliminate ambuguity.

If you run into problems of any other sort, please let me know by creating an issue on this project with full details
of how to reproduce the error.

If you have any notable successes using Typelevel Scala compiler and would like to lend your support for it then
please tweet and blog about it, and please [mail me][mail] with the details.

# Acknowledgements

[Paul Chuisano][paul] originally suggested the fairly simple Haskell-inspired algorithm implemented in the patch in a
[comment][comment] on the original ticket way back in December 2012. It's depressing to think how many hours have been
wasted working round a problem which has such a simple fix.

[si2712]: https://issues.scala-lang.org/browse/SI-2712
[tls]: https://github.com/typelevel/scala
[merged-2.12]: https://github.com/scala/scala/pull/5102
[pr-2.11]: https://github.com/scala/scala/pull/5343
[si2712fix]: https://github.com/milessabin/scala/blob/08587aa66c48c453a0aed99cceba26c655cabd65/src/reflect/scala/reflect/internal/Types.scala#L3085-L3124
[catsbuild]: https://github.com/typelevel/cats/compare/v0.4.1...milessabin:topic/si-2712
[mail]: mailto:miles@milessabin.com
[paul]: https://twitter.com/pchiusano
[comment]: https://issues.scala-lang.org/browse/SI-2712?focusedCommentId=61270
