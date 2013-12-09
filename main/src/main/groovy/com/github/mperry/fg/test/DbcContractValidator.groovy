package com.github.mperry.fg.test

import fj.F
import fj.data.Validation
import org.gcontracts.AssertionViolation
import org.gcontracts.ClassInvariantViolation
import org.gcontracts.PostconditionViolation
import org.gcontracts.PreconditionViolation

/**
 * Created with IntelliJ IDEA.
 * User: mwp
 * Date: 3/12/13
 * Time: 11:47 AM
 * To change this template use File | Settings | File Templates.
 */
class DbcContractValidator {

    static F<Validation<Throwable, Boolean>, Boolean> validateValidation() {
        Model.validator(validateThrowable())
    }

    static F<Throwable, Boolean> validateThrowable() {
        { Throwable t ->
            contractsOk(t)
        } as F
    }

    static Boolean contractViolation(Throwable t) {
        hasType(t, AssertionViolation.class)
    }

    static Boolean contractsOk(Throwable t) {
        invariantOk(t).and(preOk(t).implies(postOk(t)))
    }

    static Boolean preOk(Throwable t) {
        !hasType(t, PreconditionViolation.class)
    }

    static Boolean postOk(Throwable t) {
        !hasType(t, PostconditionViolation.class)
    }

    static Boolean invariantOk(Throwable t) {
        !hasType(t, ClassInvariantViolation.class)
    }

    static Boolean hasType(Object o, Class c) {
        c.isAssignableFrom(o.getClass())
    }

}
