package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {

        if (!isAnswerValid(answer)) return "${invalidAnswerMessage()}\n${question.question}" to status.color

        return if (question.answer.isEmpty() || question.answer.contains(answer.toLowerCase())) {
            question = question.nextQuestion()
            "Отлично - ты справился\n" +
                    (if (question == Question.IDLE) "На этом все, вопросов больше нет" else question.question) to status.color
        } else {
            status = status.nextStatus()
            if (status == Status.NORMAL) question = Question.NAME
            "Это неправильный ответ" +
                    (if (status == Status.NORMAL) ". Давай все по новой" else "") +
                    "\n${question.question}" to status.color
        }
    }

    private fun isAnswerValid(answer: String): Boolean {
        return when(question) {
            Question.NAME -> answer.matches("^[А-ЯA-Z]+.*".toRegex())
            Question.PROFESSION -> answer.matches("^[а-яa-z]+.*".toRegex())
            Question.MATERIAL -> answer.matches("^[^0-9]+$".toRegex())
            Question.BDAY -> answer.matches("^[0-9]+$".toRegex())
            Question.SERIAL -> answer.matches("^[0-9]{7}$".toRegex())
            Question.IDLE -> true
        }
    }

    private fun invalidAnswerMessage(): String {
        return when(question) {
            Question.NAME -> "Имя должно начинаться с заглавной буквы"
            Question.PROFESSION -> "Профессия должна начинаться со строчной буквы"
            Question.MATERIAL -> "Материал не должен содержать цифр"
            Question.BDAY -> "Год моего рождения должен содержать только цифры"
            Question.SERIAL -> "Серийный номер содержит только цифры, и их 7"
            Question.IDLE -> ""
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answer: List<String>) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion(): Question
    }

}