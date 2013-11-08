package com.github.mperry.fg

import fj.Unit

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 7/11/13
 * Time: 11:00 AM
 * To change this template use File | Settings | File Templates.
 */
abstract class IO1 {

	abstract Unit run()

	IO1 append(IO1 io) {
		new IO1() {
			Unit run() {
				IO1.this.run()
				return io.run()
			}
		}
	}

	static IO1 empty() {
		new IO1() {
			public Unit run() {
				return Unit.unit()
			}
		}
	}

}
