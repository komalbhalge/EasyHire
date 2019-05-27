package android.kodroid.com.easyhiring.interfaces

interface CandidateRowListener {
    fun modifyCandidateState(candidateId: String, status: Int)
}