package it.emperor.todolist.state

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    NONE;

    fun isSuccessful() = this == SUCCESS

    fun isError() = this == ERROR

    fun isLoading() = this == LOADING
}