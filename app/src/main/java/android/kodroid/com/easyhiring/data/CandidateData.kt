package android.kodroid.com.easyhiring.data

class CandidateData {
    var id:String? = null
    var status: Int = 0
    var name: String? = null
    var description: String? = null
    var profile_picture: String? = null


    constructor()
    companion object Factory {
        fun create(): CandidateData = CandidateData()
    }

    constructor(candidate_id: String?,candidate_status: Int,candidate_name: String?, candidate_description: String?, candidate_image: String?) {
        this.id= candidate_id
        this.status = candidate_status
        this.name = candidate_name
        this.description = candidate_description
        this.profile_picture = candidate_image
    }
}