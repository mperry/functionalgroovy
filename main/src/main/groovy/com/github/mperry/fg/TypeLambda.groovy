package com.github.mperry.fg

/**
 * Created by MarkPerry on 9/01/14.
 */
class TypeLambda {

    def GroovyClassLoader newLoader() {
        new GroovyClassLoader(this.class.classLoader)
//        new GroovyClassLoader()
    }

    def <S> Class<? extends State<S, ?>> partialState(GroovyClassLoader loader, Class<S> c) {
        def argName = c.simpleName
        def name = "State${argName}Dynamic"

        def s = """
            package com.github.mperry.fg

            import fj.F
            import fj.P2
            import groovy.transform.Canonical

            @Canonical
            class $name<A> extends State<$argName, A> {
                $name(F<$argName, P2<A, $argName>> f) {
                    run = f
                }
            }
        """
        def clazz = loader.parseClass(s)
//        println("parital type application: $s")
        clazz
    }

    def <A, DS> Class<? extends Monad> stateMonad(GroovyClassLoader loader, Class<? extends State> c, Class<?> ca) {
        def argName = c.simpleName
        def firstType = ca.simpleName
        def name = "${argName}Monad"

        def s =  """
            package com.github.mperry.fg

            import fj.F
            import fj.P2
            import groovy.transform.Canonical

            @Canonical
            class $name extends Monad<$argName> {
                def <B, C> $argName<C> flatMap($argName<B> mb, F<B, $argName<C>> f) {
                    new $argName<C>({ $firstType s ->
                        def p = mb.run.f(s)
                        def smc = f.f(p._1())
                        smc.run.f(p._2())
                    } as F)
                }

                def <B> $argName<B> unit(B b) {
                    new $argName<B>({ $firstType s -> P.p(b, s) } as F)
                }

            }
        """
        def clazz = loader.parseClass(s)
//        println "monad: $s"
        clazz
    }

}
