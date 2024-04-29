package uk.ac.tees.mad.d3840711
data class latestNewsResponses(
    val status: String,
    val totalResults: Int,
    val articles: List<TopNewsArticle>
)

data class TopNewsArticle(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)

data class Source(
    val id: String?,
    val name: String
)
