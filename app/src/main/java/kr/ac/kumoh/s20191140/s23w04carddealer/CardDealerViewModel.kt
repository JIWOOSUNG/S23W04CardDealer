package kr.ac.kumoh.ce.prof01.s23w04carddealer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class CardDealerViewModel : ViewModel() {
    private var _cards = MutableLiveData<IntArray>(IntArray(5) { -1 })
    val cards: LiveData<IntArray>
        get() = _cards
    fun shuffle() {
        var num = 0
        val newCards = IntArray(5) { -1 }

        for (i in newCards.indices) {
            // 중복 검사
            do {
                num = Random.nextInt(52)
            } while (newCards.contains(num))
            newCards[i] = num
        }

        // 정렬
        newCards.sort()

        _cards.value = newCards
    }

    fun getPokerHand(cards: IntArray): String {
        val cardNumbers = cards.map { it % 13 }
        val cardSuits = cards.map { it / 13 }

        if (isTop(cardNumbers)) return "탑"
        if (isOnePair(cardNumbers)) return "원 페어"
        if (isTwoPair(cardNumbers)) return "투 페어"
        if (isTriple(cardNumbers)) return "트리플"
        if (isStraight(cardNumbers)) return "스트레이트"
        if (isBackStraight(cardNumbers)) return "백 스트레이트"
        if (isMountain(cardNumbers)) return "마운틴"
        if (isFlush(cardSuits)) return "플러시"
        if (isFullHouse(cardNumbers)) return "풀 하우스"
        if (isFourCard(cardNumbers)) return "포 카드"
        if (isStraightFlush(cardNumbers, cardSuits)) return "스트레이트 플러시"
        if (isBackStraightFlush(cardNumbers, cardSuits)) return "백 스트레이트 플러시"
        if (isRoyalStraightFlush(cardNumbers, cardSuits)) return "로열 스트레이트 플러시"

        return "족보없음"
    }

    private fun isTop(cardNumbers: List<Int>): Boolean {
        // 숫자가 가장 큰 카드가 1장인 경우
        return cardNumbers.maxOrNull() == cardNumbers.last()
    }

    private fun isOnePair(cardNumbers: List<Int>): Boolean {
        // 숫자가 같은 카드가 2장인 경우
        val counts = cardNumbers.groupBy { it }.mapValues { it.value.size }
        return counts.containsValue(2)
    }
    private fun isTwoPair(cardNumbers: List<Int>): Boolean {
        // 원 페어가 2개 존재하는 경우
        val counts = cardNumbers.groupBy { it }.mapValues { it.value.size }
        return counts.filterValues { it == 2 }.size == 2
    }

    private fun isTriple(cardNumbers: List<Int>): Boolean {
        // 숫자가 같은 카드가 3장인 경우
        val counts = cardNumbers.groupBy { it }.mapValues { it.value.size }
        return counts.containsValue(3)
    }

    private fun isStraight(cardNumbers: List<Int>): Boolean {
        // 숫자가 이어지는 카드 5장인 경우
        val distinctCount = cardNumbers.distinct().size
        return distinctCount == 5 && cardNumbers.maxOrNull()!! - cardNumbers.minOrNull()!! == 4
    }

    private fun isBackStraight(cardNumbers: List<Int>): Boolean {
        // A, 2, 3, 4, 5인 경우
        return cardNumbers.containsAll(listOf(0, 9, 10, 11, 12))
    }

    private fun isMountain(cardNumbers: List<Int>): Boolean {
        // A, K, Q, J, 10인 경우
        return cardNumbers.containsAll(listOf(0, 9, 10, 11, 12))
    }

    private fun isFlush(cardSuits: List<Int>): Boolean {
        // 무늬가 같은 카드 5장인 경우
        return cardSuits.distinct().size == 1
    }

    private fun isFullHouse(cardNumbers: List<Int>): Boolean {
        // 원 페어 + 트리플 조합인 경우
        val counts = cardNumbers.groupBy { it }.mapValues { it.value.size }
        return counts.containsValue(2) && counts.containsValue(3)
    }

    private fun isFourCard(cardNumbers: List<Int>): Boolean {
        // 숫자가 같은 카드가 4장인 경우
        val counts = cardNumbers.groupBy { it }.mapValues { it.value.size }
        return counts.containsValue(4)
    }

    private fun isStraightFlush(cardNumbers: List<Int>, cardSuits: List<Int>): Boolean {
        // 숫자가 이어지고 무늬가 같은 카드 5장인 경우
        return isStraight(cardNumbers) && isFlush(cardSuits)
    }

    private fun isBackStraightFlush(cardNumbers: List<Int>, cardSuits: List<Int>): Boolean {
        // A, 2, 3, 4, 5이면서 무늬가 같은 경우
        return isBackStraight(cardNumbers) && isFlush(cardSuits)
    }

    private fun isRoyalStraightFlush(cardNumbers: List<Int>, cardSuits: List<Int>): Boolean {
        // A, K, Q, J, 10이면서 무늬가 같은 경우
        val royalCards = listOf(0, 9, 10, 11, 12)
        return cardNumbers.containsAll(royalCards) && isFlush(cardSuits)
    }

}