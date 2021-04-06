package com.lugjosh.memory

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.lugjosh.memory.models.BoardSize
import com.lugjosh.memory.models.MemoryCard
import com.lugjosh.memory.models.MemoryGame
import com.lugjosh.memory.utils.DEFAULT_ICONS
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var board: RecyclerView
    private lateinit var moves: TextView
    private lateinit var pairs: TextView
    private lateinit var root: ConstraintLayout

    private lateinit var memoryGame: MemoryGame
    private var boardSize: BoardSize = BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        board = rvBoard
        moves = tvNumMoves
        pairs = tvNumPairs
        root = clRoot

        tvNumPairs.setTextColor(ContextCompat.getColor(this,R.color.progress_none))
        memoryGame = MemoryGame(boardSize)
        adapter = MemoryBoardAdapter(this, boardSize, memoryGame.cards,
            object : MemoryBoardAdapter.CardClickListener {
                override fun onCardClicked(position: Int) {
                    updateGameFlip(position)
                }

            })
        board.adapter = adapter
        board.setHasFixedSize(true)
        board.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }

    private fun updateGameFlip(position: Int) {
        if (memoryGame.haveWonGame()) {
            Snackbar.make(root, "You already won!", Snackbar.LENGTH_LONG).show()
            return
        }

        if (memoryGame.isCardFaceUp(position)) {
            Snackbar.make(root, "Invalid move!", Snackbar.LENGTH_SHORT).show()
            return
        }

        if (memoryGame.flipCard(position)) {
            Log.i(TAG, "Found a match! Pairs found: ${memoryGame.numPairsFound}")
            val color = ArgbEvaluator().evaluate(
                memoryGame.numPairsFound.toFloat() / boardSize.getNumPairs(),
                ContextCompat.getColor(this, R.color.progress_none),
                ContextCompat.getColor(this, R.color.progress_full)
            ) as Int
            tvNumPairs.setTextColor(color)
            tvNumPairs.text = "Pairs: ${memoryGame.numPairsFound}/${boardSize.getNumPairs()}"
            if (memoryGame.haveWonGame()) {
                Snackbar.make(root, "You win! Congratulations", Snackbar.LENGTH_LONG).show()
            }
        }
        tvNumMoves.text = "Moves: ${memoryGame.getMoves()}"
        adapter.notifyDataSetChanged()
    }
}