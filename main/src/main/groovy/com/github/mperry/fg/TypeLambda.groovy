package com.github.mperry.fg

/**
 * Created by MarkPerry on 9/01/14.
 */
class TypeLambda {

    def GroovyClassLoader newLoader() {
        new GroovyClassLoader(this.class.classLoader)
//        new GroovyClassLoader()
    }

    def <S> Class<? extends StateM<S, ?>> partialStateApplication(GroovyClassLoader loader, Class<S> stateClass) {
        def stateType = stateClass.simpleName
        def name = "State${stateType}Dynamic"

        def s = """
            package com.github.mperry.fg

            import fj.F
            import fj.P2
            import groovy.transform.Canonical

            @Canonical
            class $name<A> extends StateM<$stateType, A> {

                $name(F<$stateType, P2<A, $stateType>> f) {
                    run = f
                }

                def <B> $name<B> flatMap(F<A, $name<B>> f) {
                    new $name<B>({ $stateType s ->
                        def p = run.f(s)
                        def a = p._1()
                        def s2 = p._2()
                        def sib = f.f(a)
                        sib.run.f(s2)
                    } as F)
                }

            }
        """
        def clazz = loader.parseClass(s)
//        println("parital type application: $s")
        clazz
    }

    def <S> Class<? extends Monad> stateMonad(GroovyClassLoader loader, Class<? extends StateM<S, ?>> partialStateClass, Class<S> stateClass) {
        def partialStateType = partialStateClass.simpleName
        def stateType = stateClass.simpleName
        def name = "${partialStateType}Monad"

        def s =  """
            package com.github.mperry.fg

            import fj.F
            import fj.P2
            import fj.P
            import groovy.transform.Canonical

            @Canonical
            class $name extends Monad<$partialStateType> {
                def <B, C> $partialStateType<C> flatMap($partialStateType<B> mb, F<B, $partialStateType<C>> f) {
                    mb.flatMap(f)
                }

                def <B> $partialStateType<B> unit(B b) {
                    new $partialStateType<B>({ $stateType s -> P.p(b, s) } as F)
                }

            }
        """
        def clazz = loader.parseClass(s)
//        println "monad: $s"
        clazz
    }

}
