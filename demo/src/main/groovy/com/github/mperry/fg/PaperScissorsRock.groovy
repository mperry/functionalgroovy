package com.github.mperry.fg

/**
 * Created with IntelliJ IDEA.
 * User: MarkPerry
 * Date: 22/11/13
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */

import fj.*
import fj.data.*
import fj.test.*
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

@TypeChecked
class PaperScissorsRock {

	enum Move { PAPER, SCISSORS, ROCK }
	enum MoveResult { WIN, LOSE, DRAW }

	static void main(def args) {
		new PaperScissorsRock().gameSequence()
	}

	void gameSequence() {
		gameMessages().take(20).toList().each {
			println it
		}
	}

	Stream<String> gameMessages() {
		messages().zipIndex().map { P2<String, Integer> p2 ->
			"game: ${p2._2()} ${p2._1()}"
		}
	}

	Stream<String> messages() {
		moves().zip(moves()).map { P2<Move, Move> p ->
			gameMessage(p._1(), p._2(), result(p._1(), p._2()))

		}
	}

	String gameMessage(Move m1, Move m2, MoveResult r) {
		"p1: $m1 p2: $m2 result: ${gameResultMessage(r, "p1 wins", "p2 wins", "draw")}"
	}

	String gameResultMessage(MoveResult r, String win, String lost, String draw) {
		r == MoveResult.DRAW ? draw : (r == MoveResult.WIN ? win : lost)
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	Stream<Move> moves() {
		Stream.range(1).map {
			randomMove(new Random())._2()
		}
	}

	P2<Random, Move> randomMove(Random r) {
		def v = Move.values()
		P.p(r, v[r.nextInt(v.size())])
	}

	boolean beats(Move p1, Move p2) {
		def map = [(Move.PAPER): Move.ROCK, (Move.SCISSORS): Move.PAPER, (Move.ROCK): Move.SCISSORS]
		map[p1] == p2
	}

	boolean draw(Move m1, Move m2) {
		m1 == m2
	}

	MoveResult result(Move m1, Move m2) {
		draw(m1, m2) ? MoveResult.DRAW : (beats(m1, m2) ? MoveResult.WIN : MoveResult.LOSE)
	}

}


