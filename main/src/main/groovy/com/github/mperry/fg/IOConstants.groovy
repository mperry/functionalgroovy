package com.github.mperry.fg;

import fj.Unit
import fj.data.Option
import groovy.transform.TypeChecked;

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 8/11/13
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
@TypeChecked
class IOConstants {

	static Option<Console> console() {
		Option.fromNull(System.console())
	}

	static SimpleIO<String> stdinReadLine() {
		new SimpleIO<String>() {
			String run() {
				System.in.newReader().readLine()
			}
		}
	}

	static SimpleIO<Option<String>> consoleReadLineOption() {
		new SimpleIO<Option<String>>() {
			Option<String> run() {
				Option.fromNull(System.in.withReader { Reader it ->
					it.readLine()
				})
			}
		}
	}

	static SimpleIO<Unit> stdoutWriteLine(final String msg) {
		new SimpleIO<Unit>() {
			Unit run() {
				println(msg)
				Unit.unit()
			}
		}
	}

    static SimpleIO<Unit> empty() {
        new SimpleIO<Unit>() {
            Unit run() {
                Unit.unit()
            }
        }
    }


}
