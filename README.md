# 오늘의 명언

### ViewPager2
기존 viewPager의 업데이트 버젼
recyclerView 기반 recyclerView.adapter 활용가능
~~~ kotlin
// in MainActivity.kt
private fun displayQuotesPager(quotes: List<Quote>, isNameOn: Boolean) {
	// viewPager.adapter 를 정의
	viewPager.adapter = QuotesPagerAdaptor(quotes, isNameOn)
	val startingPoint: Int = quotes.size * 10000
	// viewPager의 시작 위치를 설정, 넘기는 애니메이션 -> false
	viewPager.setCurrentItem(startingPoint, false)
}
// viewPager.adapter를 정의하기위해 QuotesPagerAdaptor class 생성
// creat QuotesPagerAdaptor.kt
class QuotesPagerAdaptor(
		private val quotes: List<Quote>,
		private val isNameOn: Boolean
): RecyclerView.Adapter<QuotesPagerAdaptor.QuoteViewHolder>() {

	// 3개의 함수를 오버라이딩(필수)
	// ViewHolder 생성 <- QuoteViewHolder class
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder
    = QuoteViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quote, parent, false)
    )

	// bind 함수로 position에 따른 text 정의
    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val actualPosition = position % quotes.size
        holder.bind(quotes[actualPosition], isNameOn)
    }

	// view? item의 갯수 -> position
    override fun getItemCount(): Int = Int.MAX_VALUE


	// QuoteViewHolder class를 정의
    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val quoteTextView: TextView = itemView.findViewById(R.id.quoteTextView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        @SuppressLint("SetTextI18n")
        fun bind(quote: Quote, isNameOn: Boolean) {
            quoteTextView.text = "\"${quote.quote}\""
            if (isNameOn) {
                nameTextView.text = "- ${quote.name}  "
                nameTextView.visibility = View.VISIBLE
            } else {
                nameTextView.visibility = View.GONE
            }
        }
    }
}
~~~

### Firebase remote-config
앱을 직접적으로 업데이트 하지않아도 업데이트 가능
~~~kotlin
// remoteConfig를 정의
val remoteConfig = Firebase.remoteConfig
// test용 Interval 설정 (실제 앱에서는 12시간 마다 업데이트 가능)
remoteConfig.setConfigSettingsAsync(
	remoteConfigSettings {
		minimumFetchIntervalInSeconds = 0
	}
// remote-config 백엔드에서 값을 가져오고 활성화(fetchAndActivate)
remoteConfig.fetchAndActivate().addOnCompleteListener {
	progressBar.visibility = View.GONE
	if (it.isSuccessful) {
		// remoteConfig.getString("quotes")
		// key = "quotes"에 해당하는 value를 가져옴
		val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
		val isNameOn = remoteConfig.getBoolean("is_name_on")
		displayQuotesPager(quotes, isNameOn)
)
~~~