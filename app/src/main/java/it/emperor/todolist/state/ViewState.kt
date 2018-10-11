package it.emperor.todolist.state

data class ViewState<ResultType>(var status: Status, var data: ResultType? = null, val errorMessage: ErrorEvent? = null) {
    companion object {
        fun <ResultType> success(data: ResultType): ViewState<ResultType> = ViewState(Status.SUCCESS, data)
        fun <ResultType> loading(): ViewState<ResultType> = ViewState(Status.LOADING)
        fun <ResultType> error(error: ErrorEvent): ViewState<ResultType> = ViewState(Status.ERROR, errorMessage = error)
    }
}