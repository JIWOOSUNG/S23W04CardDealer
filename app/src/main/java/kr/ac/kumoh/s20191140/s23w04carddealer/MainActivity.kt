package kr.ac.kumoh.s20191140.s23w04carddealer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kr.ac.kumoh.ce.prof01.s23w04carddealer.CardDealerViewModel
import kr.ac.kumoh.s20191140.s23w04carddealer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var main: ActivityMainBinding
    private lateinit var model: CardDealerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main = ActivityMainBinding.inflate(layoutInflater)
        setContentView(main.root)

        model = ViewModelProvider(this)[CardDealerViewModel::class.java]

        // 이미지뷰들을 배열로 저장
        val imageViews = arrayOf(
            main.card1, main.card2, main.card3, main.card4, main.card5
        )

        // 카드 뷰 업데이트 및 족보 판별
        model.cards.observe(this, Observer {
            val res = IntArray(5)
            for (i in it.indices) {
                res[i] = resources.getIdentifier(
                    getCardName(it[i]),
                    "drawable",
                    packageName
                )
            }

            main.card1.setImageResource(res[0])
            main.card2.setImageResource(res[1])
            main.card3.setImageResource(res[2])
            main.card4.setImageResource(res[3])
            main.card5.setImageResource(res[4])

            // 족보 판별 및 출력
            val pokerHand = model.getPokerHand(it)
            main.btnShuffle.text = pokerHand
        })

        main.btnShuffle.setOnClickListener {
            model.shuffle()
        }
    }



    private fun getCardName(c: Int) : String {
        // val에서 var로 변경
        var shape = when (c / 13) {
            0 -> "spades"
            1 -> "diamonds"
            2 -> "hearts"
            3 -> "clubs"
            else -> "error"
        }

        val number = when (c % 13) {
            0 -> "ace"
            in 1..9 -> (c % 13 + 1).toString()
            10 -> {
                shape = shape.plus("2")
                "jack"
            }
            11 -> {
                shape = shape.plus("2")
                "queen"
            }
            12 -> {
                shape = shape.plus("2")
                "king"
            }
            else -> "error"
        }

        return "c_${number}_of_${shape}"
    }
}