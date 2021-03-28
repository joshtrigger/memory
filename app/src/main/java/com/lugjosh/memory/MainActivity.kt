package com.lugjosh.memory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lugjosh.memory.models.BoardSize
import com.lugjosh.memory.models.MemoryCard
import com.lugjosh.memory.models.MemoryGame
import com.lugjosh.memory.utils.DEFAULT_ICONS
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var board: RecyclerView
    private lateinit var moves: TextView
    private lateinit var pairs: TextView

    private var boardSize: BoardSize = BoardSize.HARD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        board = rvBoard
        moves = tvNumMoves
        pairs = tvNumPairs

        val memoryGame = MemoryGame(boardSize)

        board.adapter = MemoryBoardAdapter(this, boardSize, memoryGame.cards, object : MemoryBoardAdapter.CardClickListener {
            override fun onCardClicked(position: Int) {
                Log.i(TAG, "Card  clicked $position")
            }

        })
        board.setHasFixedSize(true)
        board.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }
}