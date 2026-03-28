package com.bytik.apprentice.init

import com.bytik.apprentice.entity.Achievement
import com.bytik.apprentice.entity.AchievementCondition
import com.bytik.apprentice.entity.CodeTask
import com.bytik.apprentice.entity.Course
import com.bytik.apprentice.entity.Difficulty
import com.bytik.apprentice.entity.Lesson
import com.bytik.apprentice.entity.LessonType
import com.bytik.apprentice.entity.Module
import com.bytik.apprentice.entity.ProgressStatus
import com.bytik.apprentice.entity.QuestionType
import com.bytik.apprentice.entity.QuizOption
import com.bytik.apprentice.entity.QuizQuestion
import com.bytik.apprentice.entity.User
import com.bytik.apprentice.entity.UserAchievement
import com.bytik.apprentice.entity.UserCourseEnrollment
import com.bytik.apprentice.entity.UserProgress
import com.bytik.apprentice.entity.UserRole
import com.bytik.apprentice.repository.AchievementRepository
import com.bytik.apprentice.repository.CodeTaskRepository
import com.bytik.apprentice.repository.CourseRepository
import com.bytik.apprentice.repository.LessonRepository
import com.bytik.apprentice.repository.ModuleRepository
import com.bytik.apprentice.repository.QuizOptionRepository
import com.bytik.apprentice.repository.QuizQuestionRepository
import com.bytik.apprentice.repository.UserAchievementRepository
import com.bytik.apprentice.repository.UserCourseEnrollmentRepository
import com.bytik.apprentice.repository.UserProgressRepository
import com.bytik.apprentice.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class DataInitializer(
    private val userRepository: UserRepository,
    private val courseRepository: CourseRepository,
    private val moduleRepository: ModuleRepository,
    private val lessonRepository: LessonRepository,
    private val quizQuestionRepository: QuizQuestionRepository,
    private val quizOptionRepository: QuizOptionRepository,
    private val codeTaskRepository: CodeTaskRepository,
    private val achievementRepository: AchievementRepository,
    private val userAchievementRepository: UserAchievementRepository,
    private val userCourseEnrollmentRepository: UserCourseEnrollmentRepository,
    private val userProgressRepository: UserProgressRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    @Transactional
    override fun run(vararg args: String?) {
        if (userRepository.count() > 0) return

        seedAchievements()
        val course1 = seedPythonBeginner()
        seedPythonAdvanced()
        seedDigitalLiteracy()
        seedTestAccounts(course1)
    }

    // ─────────────────────────────────────────────────────────────
    // ACHIEVEMENTS
    // ─────────────────────────────────────────────────────────────

    private fun seedAchievements() {
        val achievements = listOf(
            Achievement(title = "Первый шаг", description = "Выполни свой первый урок", iconUrl = "🎯", conditionType = AchievementCondition.LESSONS_COMPLETED, conditionValue = 1),
            Achievement(title = "Пытливый ум", description = "Выполни 5 уроков", iconUrl = "🧠", conditionType = AchievementCondition.LESSONS_COMPLETED, conditionValue = 5),
            Achievement(title = "Знаток", description = "Выполни 10 уроков", iconUrl = "📚", conditionType = AchievementCondition.LESSONS_COMPLETED, conditionValue = 10),
            Achievement(title = "Программист", description = "Заверши свой первый курс", iconUrl = "💻", conditionType = AchievementCondition.COURSE_FINISHED, conditionValue = 1),
            Achievement(title = "Безошибочный", description = "Пройди квиз на 100%", iconUrl = "✨", conditionType = AchievementCondition.QUIZZES_PERFECT, conditionValue = 1),
            Achievement(title = "Кодер", description = "Реши 3 задачи по коду", iconUrl = "⌨️", conditionType = AchievementCondition.CODE_TASKS_COMPLETED, conditionValue = 3),
            Achievement(title = "Богач", description = "Накопи 100 байтиков", iconUrl = "💰", conditionType = AchievementCondition.BYTES_EARNED, conditionValue = 100),
            Achievement(title = "Коллекционер", description = "Получи 3 достижения", iconUrl = "🏅", conditionType = AchievementCondition.ACHIEVEMENTS_COUNT, conditionValue = 3),
            Achievement(title = "Исследователь", description = "Выполни 20 уроков", iconUrl = "🔍", conditionType = AchievementCondition.LESSONS_COMPLETED, conditionValue = 20),
            Achievement(title = "Мастер", description = "Выполни 30 уроков", iconUrl = "🏆", conditionType = AchievementCondition.LESSONS_COMPLETED, conditionValue = 30),
            Achievement(title = "Многознайка", description = "Заверши 3 курса", iconUrl = "🎓", conditionType = AchievementCondition.COURSE_FINISHED, conditionValue = 3),
            Achievement(title = "Точность", description = "Пройди 5 квизов на 100%", iconUrl = "🎯", conditionType = AchievementCondition.QUIZZES_PERFECT, conditionValue = 5),
            Achievement(title = "Хакер", description = "Реши 10 задач по коду", iconUrl = "👨‍💻", conditionType = AchievementCondition.CODE_TASKS_COMPLETED, conditionValue = 10),
            Achievement(title = "Магнат", description = "Накопи 500 байтиков", iconUrl = "💎", conditionType = AchievementCondition.BYTES_EARNED, conditionValue = 500)
        )
        achievementRepository.saveAll(achievements)
    }

    // ─────────────────────────────────────────────────────────────
    // КУРС 1: Python для начинающих
    // ─────────────────────────────────────────────────────────────

    private fun seedPythonBeginner(): Course {
        val course = courseRepository.save(
            Course(
                title = "Python для начинающих",
                description = "Изучи основы программирования на Python — один из самых популярных и простых языков в мире!",
                imageUrl = "",
                difficulty = Difficulty.BEGINNER,
                ageFrom = 7,
                ageTo = 12,
                orderIndex = 1,
                totalBytesReward = 150
            )
        )

        var idx = 1

        // ── Модуль 1: Знакомство с Python ──────────────────────────
        val m1 = moduleRepository.save(Module(course = course, title = "Знакомство с Python", description = "Первые шаги: что такое Python и как вывести текст на экран", orderIndex = 1))

        val m1l1 = lessonRepository.save(Lesson(module = m1, title = "Что такое программирование?", lessonType = LessonType.THEORY, orderIndex = idx++, xpReward = 10, bytesReward = 5, content = """
# Что такое программирование?

Программирование — это способ общаться с компьютером. Мы пишем **инструкции** на специальном языке, и компьютер их выполняет.

## Зачем учить программирование?
- Создавать игры и приложения
- Автоматизировать скучные задачи
- Понимать, как работают технологии вокруг нас

## Что такое Python?
Python — один из самых популярных языков программирования в мире. Его легко читать и писать, поэтому он идеально подходит для начинающих!

Компании Google, YouTube и NASA используют Python в своих проектах.

## Твоя первая программа

Команда `print()` выводит текст на экран:

```python
print("Привет, мир!")
```

Результат:
```
Привет, мир!
```

Всё, что ты пишешь внутри кавычек, Python напечатает на экране. Попробуй изменить текст!

```python
print("Меня зовут Аня!")
print("Я учусь программировать!")
```

## Запомни
- Команда `print()` — твой главный инструмент для вывода текста
- Текст всегда берётся в кавычки: `"текст"` или `'текст'`
- Каждая инструкция пишется на новой строке
        """.trimIndent()))

        saveQuiz(m1l1, "Что делает команда print()?",
            listOf("Выводит текст на экран" to true, "Удаляет файлы" to false, "Считает числа" to false, "Открывает браузер" to false))
        saveQuiz(m1l1, "Как правильно написать программу на Python?",
            listOf("Писать инструкции на новых строках" to true, "Писать всё в одну строку" to false, "Использовать только цифры" to false, "Не использовать скобки" to false))
        saveQuiz(m1l1, "Как выглядит первая программа?",
            listOf("""print("Привет, мир!")""" to true, """show("Привет, мир!")""" to false, """write("Привет, мир!")""" to false, """output("Привет, мир!")""" to false))
        saveQuiz(m1l1, "Где используется Python?",
            listOf("В Google, YouTube, NASA" to true, "Только в школах" to false, "Только в играх" to false, "Нигде не используется" to false))
        saveQuiz(m1l1, "Что нужно поставить вокруг текста в print()?",
            listOf("Кавычки" to true, "Скобки" to false, "Звёздочки" to false, "Тире" to false))

        val m1l2 = lessonRepository.save(Lesson(module = m1, title = "Практика: первый вывод", lessonType = LessonType.PRACTICE, orderIndex = idx++, xpReward = 15, bytesReward = 8, content = "Напиши программу, которая выводит твоё имя на экран."))
        saveCodeTask(m1l2, "Привет, мир!", "Напиши программу, которая выводит строку «Привет, Python!» на экран.", "# твой код\n", "Привет, Python!", "Используй команду print() и не забудь про кавычки.")

        val m1l3 = lessonRepository.save(Lesson(module = m1, title = "Практика: несколько строк", lessonType = LessonType.PRACTICE, orderIndex = idx++, xpReward = 15, bytesReward = 8, content = "Выведи несколько строк подряд."))
        saveCodeTask(m1l3, "Несколько print()", "Выведи три строки: своё имя, возраст и любимый цвет.", "# твой код\n", "Аня\n10\nсиний", "Каждый print() выводит текст на новой строке.")

        val m1l4 = lessonRepository.save(Lesson(module = m1, title = "Итоговый квиз модуля 1", lessonType = LessonType.QUIZ, orderIndex = idx++, xpReward = 20, bytesReward = 10, content = "Проверь знания по первому модулю!"))
        saveQuiz(m1l4, "Какая команда выводит текст?", listOf("print()" to true, "input()" to false, "show()" to false, "display()" to false))
        saveQuiz(m1l4, "Что делает Python с инструкциями?", listOf("Выполняет их по порядку" to true, "Выполняет в случайном порядке" to false, "Игнорирует" to false, "Сохраняет в файл" to false))
        saveQuiz(m1l4, "Как вывести слово Привет?", listOf("""print("Привет")""" to true, """Привет""" to false, """show Привет""" to false, """write("Привет")""" to false))
        saveQuiz(m1l4, "Сколько строк выведет: print(\"A\") print(\"B\")?", listOf("2" to true, "1" to false, "0" to false, "Ошибка" to false))
        saveQuiz(m1l4, "Python создан для чего?", listOf("Для написания программ" to true, "Для рисования картинок" to false, "Для воспроизведения музыки" to false, "Для управления мышкой" to false))

        val m1l5 = lessonRepository.save(Lesson(module = m1, title = "🎮 Мини-квест: Говорящий робот", lessonType = LessonType.PROJECT, orderIndex = idx++, xpReward = 30, bytesReward = 15, content = """
🎮 **Квест: Говорящий робот**

Ты — программист, который создаёт робота-помощника. Твой робот должен уметь представиться!

Напиши программу, которая выводит три строки:
1. «Привет! Я робот Байт.»
2. «Я умею программировать на Python.»
3. «Давай учиться вместе!»

Выполни задание и получи +15 байтиков!
        """.trimIndent()))
        saveCodeTask(m1l5, "Говорящий робот", "Выведи три строки от лица робота Байта.", "# твой код\n", "Привет! Я робот Байт.\nЯ умею программировать на Python.\nДавай учиться вместе!", "Используй три команды print().")

        // ── Модуль 2: Переменные и типы данных ─────────────────────
        val m2 = moduleRepository.save(Module(course = course, title = "Переменные и типы данных", description = "str, int, float, bool — и f-строки для вставки значений", orderIndex = 2))

        val m2l1 = lessonRepository.save(Lesson(module = m2, title = "Переменные: контейнеры для данных", lessonType = LessonType.THEORY, orderIndex = idx++, xpReward = 10, bytesReward = 5, content = """
# Переменные и типы данных

## Что такое переменная?
Переменная — это **именованный контейнер**, в котором можно хранить данные.

```python
name = "Аня"
age = 10
height = 1.45
is_student = True
```

## Типы данных в Python

| Тип | Пример | Описание |
|-----|--------|----------|
| `str` | `"Привет"` | Строка (текст) |
| `int` | `42` | Целое число |
| `float` | `3.14` | Число с точкой |
| `bool` | `True` | Истина или ложь |

## F-строки — вставка переменных в текст

```python
name = "Аня"
age = 10
print(f"Меня зовут {name}, мне {age} лет.")
```

Результат:
```
Меня зовут Аня, мне 10 лет.
```

Буква `f` перед кавычками говорит Python: «Вставь значения переменных в фигурных скобках».

## Изменение переменной

```python
score = 0
score = score + 10
print(score)  # 10
```

Переменную можно изменить в любой момент!
        """.trimIndent()))

        saveQuiz(m2l1, "Что такое переменная?", listOf("Контейнер для хранения данных" to true, "Команда вывода" to false, "Тип данных" to false, "Ошибка программы" to false))
        saveQuiz(m2l1, "Какой тип у значения 3.14?", listOf("float" to true, "int" to false, "str" to false, "bool" to false))
        saveQuiz(m2l1, "Как вставить переменную в строку?", listOf("f\"текст {переменная}\"" to true, "\"текст\" + переменная" to false, "print(переменная)" to false, "\"текст переменная\"" to false))
        saveQuiz(m2l1, "Что выведет: x = 5; print(x)?", listOf("5" to true, "x" to false, "\"x\"" to false, "Ошибку" to false))
        saveQuiz(m2l1, "Какой тип у значения True?", listOf("bool" to true, "str" to false, "int" to false, "float" to false))

        val m2l2 = lessonRepository.save(Lesson(module = m2, title = "Практика: переменные", lessonType = LessonType.PRACTICE, orderIndex = idx++, xpReward = 15, bytesReward = 8, content = "Создай переменные и выведи их."))
        saveCodeTask(m2l2, "Моя визитка", "Создай переменные name и age, присвой им своё имя и возраст, затем выведи f-строку.", "# твой код\n", "Меня зовут Аня, мне 10 лет.", "Используй f-строку с фигурными скобками.")

        val m2l3 = lessonRepository.save(Lesson(module = m2, title = "Практика: арифметика", lessonType = LessonType.PRACTICE, orderIndex = idx++, xpReward = 15, bytesReward = 8, content = "Используй переменные для вычислений."))
        saveCodeTask(m2l3, "Сумма переменных", "Создай переменные a=7 и b=3, вычисли их сумму и выведи результат через f-строку.", "# твой код\n", "7 + 3 = 10", "Используй переменную result для хранения суммы.")

        val m2l4 = lessonRepository.save(Lesson(module = m2, title = "Итоговый квиз модуля 2", lessonType = LessonType.QUIZ, orderIndex = idx++, xpReward = 20, bytesReward = 10, content = "Проверь знания по переменным и типам данных!"))
        saveQuiz(m2l4, "Как создать переменную x со значением 5?", listOf("x = 5" to true, "5 = x" to false, "var x = 5" to false, "int x = 5" to false))
        saveQuiz(m2l4, "Какой тип у \"Привет\"?", listOf("str" to true, "int" to false, "bool" to false, "float" to false))
        saveQuiz(m2l4, "Что выведет: name=\"Боб\"; print(f\"Привет, {name}!\")?", listOf("Привет, Боб!" to true, "Привет, {name}!" to false, "Привет, name!" to false, "Ошибку" to false))
        saveQuiz(m2l4, "Можно ли изменить переменную?", listOf("Да" to true, "Нет" to false, "Только один раз" to false, "Только числа" to false))
        saveQuiz(m2l4, "Что выведет: x=3; x=x+2; print(x)?", listOf("5" to true, "3" to false, "2" to false, "x+2" to false))

        val m2l5 = lessonRepository.save(Lesson(module = m2, title = "🎮 Мини-квест: Профиль героя", lessonType = LessonType.PROJECT, orderIndex = idx++, xpReward = 30, bytesReward = 15, content = """
🎮 **Квест: Профиль героя**

В игре каждый герой имеет профиль. Создай профиль своего героя!

Создай переменные:
- `hero_name` — имя героя (строка)
- `level` — уровень (целое число, начни с 1)
- `hp` — очки здоровья (целое число, например 100)

Выведи профиль в виде трёх строк с f-строками:
```
Герой: <имя>
Уровень: <уровень>
HP: <очки здоровья>
```
        """.trimIndent()))
        saveCodeTask(m2l5, "Профиль героя", "Создай переменные hero_name, level, hp и выведи профиль.", "# твой код\n", "Герой: Дракон\nУровень: 1\nHP: 100", "Используй f-строки для каждой строки вывода.")

        // ── Модуль 3: Условные операторы ────────────────────────────
        val m3 = moduleRepository.save(Module(course = course, title = "Условные операторы", description = "if, elif, else — принятие решений в программе", orderIndex = 3))

        val m3l1 = lessonRepository.save(Lesson(module = m3, title = "if, elif, else", lessonType = LessonType.THEORY, orderIndex = idx++, xpReward = 10, bytesReward = 5, content = """
# Условные операторы

## Зачем нужны условия?
Программы должны принимать решения. Например: «Если число больше 10, выведи "большое", иначе — "маленькое"».

## Синтаксис if

```python
age = 12

if age >= 10:
    print("Тебе уже 10 или больше!")
```

**Важно:** после `if` ставится `:` и следующая строка делает отступ (4 пробела или Tab).

## if-else

```python
score = 75

if score >= 60:
    print("Сдал!")
else:
    print("Не сдал.")
```

## if-elif-else

```python
grade = 85

if grade >= 90:
    print("Отлично!")
elif grade >= 70:
    print("Хорошо!")
elif grade >= 50:
    print("Удовлетворительно")
else:
    print("Надо постараться")
```

## Операторы сравнения

| Оператор | Значение |
|----------|----------|
| `==` | равно |
| `!=` | не равно |
| `>` | больше |
| `<` | меньше |
| `>=` | больше или равно |
| `<=` | меньше или равно |

## Логические операторы

```python
x = 15
if x > 10 and x < 20:
    print("Число между 10 и 20")
```

`and` — оба условия должны быть истинными.
`or` — достаточно одного истинного.
`not` — инвертирует условие.
        """.trimIndent()))

        saveQuiz(m3l1, "Что делает оператор if?", listOf("Проверяет условие и выполняет блок кода" to true, "Выводит текст" to false, "Создаёт переменную" to false, "Повторяет код" to false))
        saveQuiz(m3l1, "Что значит ==?", listOf("Проверка равенства" to true, "Присвоение значения" to false, "Больше или равно" to false, "Не равно" to false))
        saveQuiz(m3l1, "Что будет, если условие if ложно и нет else?", listOf("Блок if пропустится" to true, "Ошибка программы" to false, "Программа остановится" to false, "Условие выполнится всё равно" to false))
        saveQuiz(m3l1, "Что означает оператор and?", listOf("Оба условия должны быть истинными" to true, "Достаточно одного истинного" to false, "Инвертирует условие" to false, "Сравнивает строки" to false))
        saveQuiz(m3l1, "Сколько блоков elif можно использовать?", listOf("Сколько угодно" to true, "Только один" to false, "Только два" to false, "Ни одного" to false))

        val m3l2 = lessonRepository.save(Lesson(module = m3, title = "Практика: чётное или нечётное", lessonType = LessonType.PRACTICE, orderIndex = idx++, xpReward = 15, bytesReward = 8, content = "Определи, чётное число или нечётное."))
        saveCodeTask(m3l2, "Чётное/нечётное", "Создай переменную num=7. Если num % 2 == 0 — выведи «Чётное», иначе — «Нечётное».", "# твой код\n", "Нечётное", "Используй оператор % для получения остатка от деления.")

        val m3l3 = lessonRepository.save(Lesson(module = m3, title = "Практика: оценка по баллам", lessonType = LessonType.PRACTICE, orderIndex = idx++, xpReward = 15, bytesReward = 8, content = "Определи оценку по количеству баллов."))
        saveCodeTask(m3l3, "Оценка по баллам", "score=80. Выведи «Отлично» если >=90, «Хорошо» если >=70, «Плохо» иначе.", "# твой код\n", "Хорошо", "Используй if-elif-else.")

        val m3l4 = lessonRepository.save(Lesson(module = m3, title = "Итоговый квиз модуля 3", lessonType = LessonType.QUIZ, orderIndex = idx++, xpReward = 20, bytesReward = 10, content = "Проверь знания об условных операторах!"))
        saveQuiz(m3l4, "Что ставится в конце строки с if?", listOf("Двоеточие :" to true, "Точка с запятой ;" to false, "Скобка )" to false, "Ничего" to false))
        saveQuiz(m3l4, "Что делает elif?", listOf("Проверяет дополнительное условие" to true, "Всегда выполняется" to false, "Останавливает программу" to false, "Повторяет блок" to false))
        saveQuiz(m3l4, "Что выведет: x=5; if x>3: print(\"Да\") else: print(\"Нет\")?", listOf("Да" to true, "Нет" to false, "5" to false, "Ошибку" to false))
        saveQuiz(m3l4, "Что означает !=?", listOf("Не равно" to true, "Равно" to false, "Больше" to false, "Меньше или равно" to false))
        saveQuiz(m3l4, "True or False — это что?", listOf("Значения типа bool" to true, "Строки" to false, "Числа" to false, "Команды" to false))

        val m3l5 = lessonRepository.save(Lesson(module = m3, title = "🎮 Мини-квест: Страж ворот", lessonType = LessonType.PROJECT, orderIndex = idx++, xpReward = 30, bytesReward = 15, content = """
🎮 **Квест: Страж ворот**

Ты — программист магического замка. Нужно написать программу для стража ворот.

Правила:
- Если возраст >= 18: выведи «Добро пожаловать, взрослый путник!»
- Если возраст >= 12: выведи «Привет, юный герой! Проходи.»
- Иначе: выведи «Малыш, тебе ещё рано. Возвращайся через несколько лет!»

Создай переменную `visitor_age = 14` и напиши условия.
        """.trimIndent()))
        saveCodeTask(m3l5, "Страж ворот", "Напиши программу стража ворот для visitor_age=14.", "# твой код\n", "Привет, юный герой! Проходи.", "Используй if-elif-else с правильными условиями.")

        // ── Модуль 4: Циклы ──────────────────────────────────────────
        val m4 = moduleRepository.save(Module(course = course, title = "Циклы", description = "for, while, range — повторяем действия в программе", orderIndex = 4))

        val m4l1 = lessonRepository.save(Lesson(module = m4, title = "Циклы for и while", lessonType = LessonType.THEORY, orderIndex = idx++, xpReward = 10, bytesReward = 5, content = """
# Циклы

## Зачем нужны циклы?
Циклы позволяют **повторять** блок кода много раз, не копируя его.

## Цикл for

```python
for i in range(5):
    print(i)
```

Выведет:
```
0
1
2
3
4
```

`range(5)` создаёт числа от 0 до 4 (5 не включается).

```python
for i in range(1, 6):
    print(i)
```
Выведет числа от 1 до 5.

## Перебор строки

```python
for letter in "Python":
    print(letter)
```

## Цикл while

```python
count = 0
while count < 5:
    print(count)
    count = count + 1
```

while повторяет блок, **пока** условие истинно.

## break — выход из цикла

```python
for i in range(10):
    if i == 5:
        break
    print(i)
```

Выведет числа от 0 до 4, затем остановится.

## Пример: сумма чисел

```python
total = 0
for i in range(1, 6):
    total = total + i
print(f"Сумма: {total}")  # Сумма: 15
```
        """.trimIndent()))

        saveQuiz(m4l1, "Что делает цикл for?", listOf("Повторяет блок кода для каждого элемента" to true, "Проверяет условие" to false, "Создаёт переменную" to false, "Выводит текст" to false))
        saveQuiz(m4l1, "Что выведет range(3)?", listOf("0, 1, 2" to true, "1, 2, 3" to false, "0, 1, 2, 3" to false, "3" to false))
        saveQuiz(m4l1, "Когда используют while?", listOf("Когда неизвестно точное число повторений" to true, "Всегда вместо for" to false, "Только для строк" to false, "Только для чисел" to false))
        saveQuiz(m4l1, "Что делает break?", listOf("Прерывает цикл" to true, "Пропускает итерацию" to false, "Повторяет цикл" to false, "Выводит текст" to false))
        saveQuiz(m4l1, "Что выведет: for i in range(2,5): print(i)?", listOf("2, 3, 4" to true, "2, 3, 4, 5" to false, "0, 1, 2" to false, "2, 4" to false))

        val m4l2 = lessonRepository.save(Lesson(module = m4, title = "Практика: таблица умножения", lessonType = LessonType.PRACTICE, orderIndex = idx++, xpReward = 15, bytesReward = 8, content = "Выведи таблицу умножения на 3."))
        saveCodeTask(m4l2, "Таблица умножения", "Используя цикл for, выведи таблицу умножения числа 3 от 1 до 5.", "# твой код\n", "3 x 1 = 3\n3 x 2 = 6\n3 x 3 = 9\n3 x 4 = 12\n3 x 5 = 15", "Используй f-строку внутри цикла for.")

        val m4l3 = lessonRepository.save(Lesson(module = m4, title = "Практика: сумма чисел", lessonType = LessonType.PRACTICE, orderIndex = idx++, xpReward = 15, bytesReward = 8, content = "Подсчитай сумму чисел от 1 до 10."))
        saveCodeTask(m4l3, "Сумма 1 до 10", "Используй цикл for и переменную total, чтобы подсчитать сумму чисел от 1 до 10. Выведи результат.", "# твой код\n", "55", "Начни с total=0 и прибавляй i на каждой итерации.")

        val m4l4 = lessonRepository.save(Lesson(module = m4, title = "Итоговый квиз модуля 4", lessonType = LessonType.QUIZ, orderIndex = idx++, xpReward = 20, bytesReward = 10, content = "Проверь знания о циклах!"))
        saveQuiz(m4l4, "Чем отличается for от while?", listOf("for — для известного числа повторений, while — для условия" to true, "Они одинаковы" to false, "while быстрее" to false, "for только для строк" to false))
        saveQuiz(m4l4, "Что будет если условие while всегда True?", listOf("Бесконечный цикл" to true, "Программа завершится" to false, "Ошибка" to false, "Цикл выполнится один раз" to false))
        saveQuiz(m4l4, "Что делает range(2, 8, 2)?", listOf("Числа 2, 4, 6" to true, "Числа 2, 4, 6, 8" to false, "Числа 0, 2, 4, 6" to false, "Числа 8, 6, 4, 2" to false))
        saveQuiz(m4l4, "Как называется переменная-счётчик в for i in range()?", listOf("i (итератор)" to true, "range" to false, "for" to false, "count" to false))
        saveQuiz(m4l4, "Что выведет: total=0; for i in [1,2,3]: total+=i; print(total)?", listOf("6" to true, "3" to false, "0" to false, "1" to false))

        // ── Модуль 5: Итоговый проект ────────────────────────────────
        val m5 = moduleRepository.save(Module(course = course, title = "Итоговый проект", description = "Применяем всё что изучили — финальный тест и проект Калькулятор", orderIndex = 5))

        val m5l1 = lessonRepository.save(Lesson(module = m5, title = "Финальный тест курса", lessonType = LessonType.QUIZ, orderIndex = idx++, xpReward = 40, bytesReward = 20, content = "Итоговый тест по всему курсу Python для начинающих!"))
        saveQuiz(m5l1, "Какая команда выводит текст на экран?", listOf("print()" to true, "show()" to false, "write()" to false, "display()" to false))
        saveQuiz(m5l1, "Что такое переменная?", listOf("Контейнер для хранения данных" to true, "Команда Python" to false, "Тип данных" to false, "Цикл" to false))
        saveQuiz(m5l1, "Как вставить переменную в строку?", listOf("f\"{переменная}\"" to true, "str(переменная)" to false, "#переменная#" to false, "$(переменная)" to false))
        saveQuiz(m5l1, "Что делает if?", listOf("Выполняет блок если условие истинно" to true, "Повторяет блок" to false, "Создаёт переменную" to false, "Останавливает программу" to false))
        saveQuiz(m5l1, "Что выведет range(1,4)?", listOf("1, 2, 3" to true, "1, 2, 3, 4" to false, "0, 1, 2, 3" to false, "4" to false))
        saveQuiz(m5l1, "Как проверить равенство двух значений?", listOf("==" to true, "=" to false, "!=" to false, ">=" to false))
        saveQuiz(m5l1, "Какой тип у значения \"Привет\"?", listOf("str" to true, "int" to false, "bool" to false, "float" to false))
        saveQuiz(m5l1, "Что делает break в цикле?", listOf("Прерывает цикл" to true, "Пропускает итерацию" to false, "Перезапускает цикл" to false, "Ничего" to false))
        saveQuiz(m5l1, "Что выведет: x=2; while x<5: print(x); x+=1?", listOf("2, 3, 4" to true, "2, 3, 4, 5" to false, "0, 1, 2" to false, "Бесконечный цикл" to false))
        saveQuiz(m5l1, "Что означает elif?", listOf("Иначе если" to true, "Всегда" to false, "Конец цикла" to false, "Начало функции" to false))

        val m5l2 = lessonRepository.save(Lesson(module = m5, title = "🎮 Финальный проект: Калькулятор", lessonType = LessonType.PROJECT, orderIndex = idx++, xpReward = 50, bytesReward = 25, content = """
🎮 **Финальный проект: Калькулятор**

Поздравляем! Ты дошёл до финального задания!

Создай простой калькулятор. Используй переменные a и b:
- `a = 10`, `b = 3`

Выведи результаты четырёх операций:
```
10 + 3 = 13
10 - 3 = 7
10 * 3 = 30
10 / 3 = 3.3333333333333335
```

Используй f-строки для вывода каждой операции.

После завершения ты получишь сертификат о прохождении курса! 🏆
        """.trimIndent()))
        saveCodeTask(m5l2, "Калькулятор", "Создай калькулятор с переменными a=10 и b=3, выведи результаты 4 операций.", "# твой код\n", "10 + 3 = 13\n10 - 3 = 7\n10 * 3 = 30\n10 / 3 = 3.3333333333333335", "Используй f-строки и операторы +, -, *, /")

        return course
    }

    // ─────────────────────────────────────────────────────────────
    // КУРС 2: Python продвинутый
    // ─────────────────────────────────────────────────────────────

    private fun seedPythonAdvanced() {
        val course = courseRepository.save(
            Course(
                title = "Python продвинутый",
                description = "Изучи списки, функции и словари — инструменты настоящего программиста!",
                imageUrl = "",
                difficulty = Difficulty.INTERMEDIATE,
                ageFrom = 13,
                ageTo = 15,
                orderIndex = 2,
                totalBytesReward = 200
            )
        )

        var idx = 1

        // ── Модуль 1: Списки ─────────────────────────────────────────
        val m1 = moduleRepository.save(Module(course = course, title = "Списки", description = "Создание, индексация, append, pop — работа с коллекциями данных", orderIndex = 1))

        val m1l1 = lessonRepository.save(Lesson(module = m1, title = "Списки в Python", lessonType = LessonType.THEORY, orderIndex = idx++, xpReward = 10, bytesReward = 5, content = """
# Списки в Python

## Что такое список?
Список (list) — это **упорядоченная коллекция** значений. В одном списке можно хранить много данных.

```python
fruits = ["яблоко", "банан", "апельсин"]
numbers = [1, 2, 3, 4, 5]
mixed = ["Привет", 42, True, 3.14]
```

## Индексация

Каждый элемент списка имеет **индекс** — номер позиции (начиная с 0):

```python
fruits = ["яблоко", "банан", "апельсин"]
print(fruits[0])  # яблоко
print(fruits[1])  # банан
print(fruits[2])  # апельсин
print(fruits[-1]) # апельсин (последний)
```

## Изменение элементов

```python
fruits[0] = "груша"
print(fruits)  # ['груша', 'банан', 'апельсин']
```

## Методы списка

```python
fruits.append("виноград")   # добавить в конец
fruits.pop()                # удалить последний
fruits.pop(0)               # удалить по индексу
print(len(fruits))          # длина списка
```

## Перебор списка

```python
for fruit in fruits:
    print(fruit)
```

## Срезы

```python
numbers = [0, 1, 2, 3, 4, 5]
print(numbers[1:4])   # [1, 2, 3]
print(numbers[:3])    # [0, 1, 2]
print(numbers[3:])    # [3, 4, 5]
```
        """.trimIndent()))

        saveQuiz(m1l1, "С какого индекса начинается список?", listOf("С 0" to true, "С 1" to false, "С -1" to false, "С любого" to false))
        saveQuiz(m1l1, "Что делает append()?", listOf("Добавляет элемент в конец" to true, "Удаляет элемент" to false, "Возвращает длину" to false, "Сортирует список" to false))
        saveQuiz(m1l1, "Что вернёт len([1,2,3,4])?", listOf("4" to true, "3" to false, "0" to false, "Ошибку" to false))
        saveQuiz(m1l1, "Как получить последний элемент списка?", listOf("list[-1]" to true, "list[last]" to false, "list[end]" to false, "list.last()" to false))
        saveQuiz(m1l1, "Что делает pop()?", listOf("Удаляет и возвращает элемент" to true, "Добавляет элемент" to false, "Сортирует список" to false, "Очищает список" to false))

        val m1l2 = lessonRepository.save(Lesson(module = m1, title = "Практика: работа со списком", lessonType = LessonType.PRACTICE, orderIndex = idx++, xpReward = 15, bytesReward = 8, content = "Создай список и выполни операции."))
        saveCodeTask(m1l2, "Список фруктов", "Создай список из 3 фруктов. Добавь ещё один через append. Выведи первый и последний элемент.", "# твой код\n", "яблоко\nвиноград", "Используй append() и индексы [0] и [-1].")

        val m1l3 = lessonRepository.save(Lesson(module = m1, title = "Практика: сумма элементов", lessonType = LessonType.PRACTICE, orderIndex = idx++, xpReward = 15, bytesReward = 8, content = "Подсчитай сумму всех элементов списка."))
        saveCodeTask(m1l3, "Сумма списка", "Создай список numbers=[1,2,3,4,5]. Подсчитай сумму перебором и выведи результат.", "# твой код\n", "15", "Используй цикл for и переменную-аккумулятор.")

        val m1l4 = lessonRepository.save(Lesson(module = m1, title = "Итоговый квиз модуля 1", lessonType = LessonType.QUIZ, orderIndex = idx++, xpReward = 20, bytesReward = 10, content = "Проверь знания о списках!"))
        saveQuiz(m1l4, "Как создать пустой список?", listOf("[]" to true, "{}" to false, "()" to false, "list" to false))
        saveQuiz(m1l4, "Что вернёт [10,20,30][1]?", listOf("20" to true, "10" to false, "30" to false, "Ошибку" to false))
        saveQuiz(m1l4, "Как добавить элемент в список?", listOf("list.append(x)" to true, "list.add(x)" to false, "list + x" to false, "list.insert(x)" to false))
        saveQuiz(m1l4, "Что такое срез [1:3]?", listOf("Элементы с индекса 1 до 2" to true, "Элементы с 1 до 3 включительно" to false, "Первые 3 элемента" to false, "Последние 3 элемента" to false))
        saveQuiz(m1l4, "Как перебрать все элементы списка?", listOf("for x in list:" to true, "while list:" to false, "foreach x in list:" to false, "loop list:" to false))

        val m1l5 = lessonRepository.save(Lesson(module = m1, title = "🎮 Мини-квест: Инвентарь героя", lessonType = LessonType.PROJECT, orderIndex = idx++, xpReward = 30, bytesReward = 15, content = """
🎮 **Квест: Инвентарь героя**

Твой герой исследует подземелье и собирает предметы.

Создай список `inventory = ["меч", "щит", "зелье"]`.

Затем:
1. Добавь в инвентарь «магический посох»
2. Выведи все предметы с нумерацией (используй enumerate)
3. Выведи общее количество предметов

Ожидаемый вывод:
```
1: меч
2: щит
3: зелье
4: магический посох
Всего предметов: 4
```
        """.trimIndent()))
        saveCodeTask(m1l5, "Инвентарь героя", "Создай инвентарь, добавь предмет, выведи с нумерацией и итогом.", "# твой код\n", "1: меч\n2: щит\n3: зелье\n4: магический посох\nВсего предметов: 4", "Используй enumerate(inventory, 1) для нумерации с 1.")

        // ── Модуль 2: Функции ────────────────────────────────────────
        val m2 = moduleRepository.save(Module(course = course, title = "Функции", description = "def, return, аргументы — создаём переиспользуемые блоки кода", orderIndex = 2))

        val m2l1 = lessonRepository.save(Lesson(module = m2, title = "Функции в Python", lessonType = LessonType.THEORY, orderIndex = idx++, xpReward = 10, bytesReward = 5, content = """
# Функции в Python

## Что такое функция?
Функция — это **именованный блок кода**, который можно вызвать несколько раз.

```python
def greet():
    print("Привет!")

greet()  # Вызов функции
greet()  # Ещё раз
```

## Функции с аргументами

```python
def greet(name):
    print(f"Привет, {name}!")

greet("Аня")   # Привет, Аня!
greet("Боб")   # Привет, Боб!
```

## Функции с несколькими аргументами

```python
def add(a, b):
    return a + b

result = add(3, 5)
print(result)  # 8
```

## return — возврат значения

```python
def square(x):
    return x * x

y = square(4)
print(y)  # 16
```

## Аргументы по умолчанию

```python
def greet(name, greeting="Привет"):
    print(f"{greeting}, {name}!")

greet("Аня")           # Привет, Аня!
greet("Боб", "Здравствуй")  # Здравствуй, Боб!
```

## Зачем нужны функции?
- Избегаем повторения кода (DRY — Don't Repeat Yourself)
- Разбиваем сложную задачу на части
- Делаем код понятным и структурированным
        """.trimIndent()))

        saveQuiz(m2l1, "Как объявить функцию?", listOf("def имя():" to true, "function имя():" to false, "fun имя():" to false, "func имя():" to false))
        saveQuiz(m2l1, "Что делает return?", listOf("Возвращает значение из функции" to true, "Выводит значение" to false, "Прерывает программу" to false, "Создаёт переменную" to false))
        saveQuiz(m2l1, "Как называются входные данные функции?", listOf("Аргументы (параметры)" to true, "Переменные" to false, "Возвращаемые значения" to false, "Условия" to false))
        saveQuiz(m2l1, "Что выведет: def f(x): return x*2; print(f(5))?", listOf("10" to true, "5" to false, "2" to false, "x*2" to false))
        saveQuiz(m2l1, "Что означает DRY?", listOf("Don't Repeat Yourself" to true, "Do Run Yourself" to false, "Don't Run Yet" to false, "Do Repeat Yesterday" to false))

        val m2l2 = lessonRepository.save(Lesson(module = m2, title = "Практика: функция приветствия", lessonType = LessonType.PRACTICE, orderIndex = idx++, xpReward = 15, bytesReward = 8, content = "Напиши функцию с аргументом."))
        saveCodeTask(m2l2, "Функция приветствия", "Напиши функцию greet(name), которая выводит «Привет, <name>!». Вызови её с именем «Питон».", "# твой код\n", "Привет, Питон!", "Используй f-строку внутри функции.")

        val m2l3 = lessonRepository.save(Lesson(module = m2, title = "Практика: функция вычисления", lessonType = LessonType.PRACTICE, orderIndex = idx++, xpReward = 15, bytesReward = 8, content = "Напиши функцию с return."))
        saveCodeTask(m2l3, "Функция максимума", "Напиши функцию max_of_two(a, b), которая возвращает большее из двух чисел. Выведи max_of_two(7, 3).", "# твой код\n", "7", "Используй if-else и return внутри функции.")

        val m2l4 = lessonRepository.save(Lesson(module = m2, title = "Итоговый квиз модуля 2", lessonType = LessonType.QUIZ, orderIndex = idx++, xpReward = 20, bytesReward = 10, content = "Проверь знания о функциях!"))
        saveQuiz(m2l4, "Как вызвать функцию foo()?", listOf("foo()" to true, "call foo()" to false, "run foo" to false, "invoke foo" to false))
        saveQuiz(m2l4, "Что произойдёт без return в функции?", listOf("Функция вернёт None" to true, "Ошибка компиляции" to false, "Функция вернёт 0" to false, "Функция не вызовется" to false))
        saveQuiz(m2l4, "Можно ли вызвать функцию до её объявления?", listOf("Нет" to true, "Да" to false, "Только если она без аргументов" to false, "Только в циклах" to false))
        saveQuiz(m2l4, "Что такое аргумент по умолчанию?", listOf("Значение, используемое если аргумент не передан" to true, "Обязательный аргумент" to false, "Тип аргумента" to false, "Имя функции" to false))
        saveQuiz(m2l4, "Сколько значений может вернуть return?", listOf("Одно (или кортеж)" to true, "Только одно" to false, "Не может ничего вернуть" to false, "Только числа" to false))

        val m2l5 = lessonRepository.save(Lesson(module = m2, title = "🎮 Мини-квест: Магические заклинания", lessonType = LessonType.PROJECT, orderIndex = idx++, xpReward = 30, bytesReward = 15, content = """
🎮 **Квест: Магические заклинания**

Ты — волшебник-программист. Каждое заклинание — это функция!

Напиши три функции:
1. `fire_ball(damage)` — выводит «Огненный шар наносит <damage> урона!»
2. `heal(hp)` — выводит «Исцеление восстанавливает <hp> HP!»
3. `shield()` — выводит «Щит активирован!'»

Вызови все три функции с аргументами: fire_ball(50), heal(30), shield()
        """.trimIndent()))
        saveCodeTask(m2l5, "Магические заклинания", "Напиши три функции заклинаний и вызови их.", "# твой код\n", "Огненный шар наносит 50 урона!\nИсцеление восстанавливает 30 HP!\nЩит активирован!", "Объяви функции через def, используй f-строки с аргументами.")

        // ── Модуль 3: Словари ────────────────────────────────────────
        val m3 = moduleRepository.save(Module(course = course, title = "Словари", description = "Ключ-значение, перебор словаря — мощная структура данных", orderIndex = 3))

        val m3l1 = lessonRepository.save(Lesson(module = m3, title = "Словари в Python", lessonType = LessonType.THEORY, orderIndex = idx++, xpReward = 10, bytesReward = 5, content = """
# Словари в Python

## Что такое словарь?
Словарь (dict) — это коллекция пар **ключ: значение**.

```python
person = {
    "name": "Аня",
    "age": 13,
    "city": "Москва"
}
```

## Доступ к значениям

```python
print(person["name"])  # Аня
print(person["age"])   # 13
```

## Изменение и добавление

```python
person["age"] = 14          # изменить
person["hobby"] = "Python"  # добавить новый ключ
```

## Удаление

```python
del person["city"]
```

## Методы словаря

```python
print(person.keys())    # все ключи
print(person.values())  # все значения
print(person.items())   # все пары
```

## Перебор словаря

```python
for key, value in person.items():
    print(f"{key}: {value}")
```

## Проверка наличия ключа

```python
if "name" in person:
    print("Ключ существует!")
```

## Пример: счётчик слов

```python
text = "яблоко банан яблоко"
counter = {}
for word in text.split():
    if word in counter:
        counter[word] += 1
    else:
        counter[word] = 1
print(counter)  # {'яблоко': 2, 'банан': 1}
```
        """.trimIndent()))

        saveQuiz(m3l1, "Как создать словарь?", listOf("{ключ: значение}" to true, "[ключ: значение]" to false, "(ключ: значение)" to false, "dict(ключ=значение)" to false))
        saveQuiz(m3l1, "Как получить значение по ключу?", listOf("dict[\"ключ\"]" to true, "dict.get[\"ключ\"]" to false, "dict[0]" to false, "dict->ключ" to false))
        saveQuiz(m3l1, "Что возвращает dict.keys()?", listOf("Все ключи словаря" to true, "Все значения" to false, "Длину словаря" to false, "Первый ключ" to false))
        saveQuiz(m3l1, "Как проверить наличие ключа?", listOf("\"ключ\" in dict" to true, "dict.has(\"ключ\")" to false, "dict.contains(\"ключ\")" to false, "dict[\"ключ\"] == True" to false))
        saveQuiz(m3l1, "Как перебрать пары ключ-значение?", listOf("for k, v in dict.items():" to true, "for k in dict.keys():" to false, "for v in dict.values():" to false, "for k, v in dict:" to false))

        val m3l2 = lessonRepository.save(Lesson(module = m3, title = "Практика: профиль пользователя", lessonType = LessonType.PRACTICE, orderIndex = idx++, xpReward = 15, bytesReward = 8, content = "Создай словарь и выведи информацию."))
        saveCodeTask(m3l2, "Профиль пользователя", "Создай словарь user с ключами name, age, city. Выведи все пары через items().", "# твой код\n", "name: Аня\nage: 13\ncity: Москва", "Используй цикл for с .items().")

        val m3l3 = lessonRepository.save(Lesson(module = m3, title = "Практика: счётчик букв", lessonType = LessonType.PRACTICE, orderIndex = idx++, xpReward = 15, bytesReward = 8, content = "Подсчитай сколько раз встречается каждая буква."))
        saveCodeTask(m3l3, "Счётчик букв", "Для строки word=\"python\" посчитай сколько раз встречается каждая буква и выведи словарь.", "# твой код\n", "{'p': 1, 'y': 1, 't': 1, 'h': 1, 'o': 1, 'n': 1}", "Используй словарь как счётчик.")

        val m3l4 = lessonRepository.save(Lesson(module = m3, title = "Итоговый квиз модуля 3", lessonType = LessonType.QUIZ, orderIndex = idx++, xpReward = 20, bytesReward = 10, content = "Проверь знания о словарях!"))
        saveQuiz(m3l4, "Могут ли ключи словаря повторяться?", listOf("Нет, ключи уникальны" to true, "Да, могут" to false, "Только числа могут" to false, "Только строки не могут" to false))
        saveQuiz(m3l4, "Что произойдёт при dict[несуществующий ключ]?", listOf("KeyError" to true, "None" to false, "0" to false, "Пустая строка" to false))
        saveQuiz(m3l4, "Чем словарь отличается от списка?", listOf("Доступ по ключу, а не по индексу" to true, "Словарь быстрее" to false, "Список упорядочен, словарь нет" to false, "Словарь хранит только строки" to false))
        saveQuiz(m3l4, "Как добавить новую пару в словарь?", listOf("dict[\"ключ\"] = значение" to true, "dict.add(\"ключ\", значение)" to false, "dict.append(\"ключ\", значение)" to false, "dict.insert(\"ключ\", значение)" to false))
        saveQuiz(m3l4, "Что делает del dict[\"ключ\"]?", listOf("Удаляет пару ключ-значение" to true, "Удаляет весь словарь" to false, "Обнуляет значение" to false, "Вызывает ошибку" to false))

        // ── Модуль 4: Итоговый проект ────────────────────────────────
        val m4 = moduleRepository.save(Module(course = course, title = "Итоговый проект", description = "Финальный тест и комплексный проект с использованием всех тем", orderIndex = 4))

        val m4l1 = lessonRepository.save(Lesson(module = m4, title = "Финальный тест курса", lessonType = LessonType.QUIZ, orderIndex = idx++, xpReward = 40, bytesReward = 20, content = "Итоговый тест по всему курсу Python продвинутый!"))
        saveQuiz(m4l1, "Что такое список в Python?", listOf("Упорядоченная коллекция значений" to true, "Пара ключ-значение" to false, "Именованный блок кода" to false, "Тип данных" to false))
        saveQuiz(m4l1, "Что делает list.append(x)?", listOf("Добавляет x в конец списка" to true, "Удаляет x из списка" to false, "Вставляет x в начало" to false, "Сортирует список" to false))
        saveQuiz(m4l1, "Что возвращает функция без return?", listOf("None" to true, "0" to false, "False" to false, "Ошибку" to false))
        saveQuiz(m4l1, "Как объявить функцию с двумя аргументами?", listOf("def f(a, b):" to true, "def f[a, b]:" to false, "function f(a, b):" to false, "f(a, b):" to false))
        saveQuiz(m4l1, "Какой символ разделяет ключ и значение в словаре?", listOf(":" to true, "=" to false, "," to false, ";" to false))
        saveQuiz(m4l1, "Что выведет [1,2,3][2]?", listOf("3" to true, "2" to false, "1" to false, "Ошибку" to false))
        saveQuiz(m4l1, "Как проверить наличие элемента в списке?", listOf("x in list" to true, "list.contains(x)" to false, "list.has(x)" to false, "x == list" to false))
        saveQuiz(m4l1, "Что делает dict.values()?", listOf("Возвращает все значения" to true, "Возвращает все ключи" to false, "Удаляет все значения" to false, "Сортирует значения" to false))
        saveQuiz(m4l1, "Можно ли передать список в функцию?", listOf("Да" to true, "Нет" to false, "Только числа" to false, "Только строки" to false))
        saveQuiz(m4l1, "Что такое индекс -1?", listOf("Последний элемент" to true, "Ошибка" to false, "Первый элемент" to false, "Длина минус один" to false))

        val m4l2 = lessonRepository.save(Lesson(module = m4, title = "🎮 Финальный проект: База данных героев", lessonType = LessonType.PROJECT, orderIndex = idx++, xpReward = 50, bytesReward = 25, content = """
🎮 **Финальный проект: База данных героев**

Ты создаёшь систему учёта героев для RPG-игры!

Создай функцию `show_hero(hero)`, которая принимает словарь героя и выводит его данные.

Создай список из двух героев-словарей:
- Герой 1: name="Артур", class="Воин", level=5, hp=120
- Герой 2: name="Мерлин", class="Маг", level=7, hp=80

Вызови функцию для каждого героя в цикле.

Ожидаемый вывод:
```
=== Герой ===
Имя: Артур
Класс: Воин
Уровень: 5
HP: 120
=== Герой ===
Имя: Мерлин
Класс: Маг
Уровень: 7
HP: 80
```
        """.trimIndent()))
        saveCodeTask(m4l2, "База данных героев", "Создай функцию show_hero и список из двух героев.", "# твой код\n", "=== Герой ===\nИмя: Артур\nКласс: Воин\nУровень: 5\nHP: 120\n=== Герой ===\nИмя: Мерлин\nКласс: Маг\nУровень: 7\nHP: 80", "Используй функцию, список словарей и цикл for.")
    }

    // ─────────────────────────────────────────────────────────────
    // КУРС 3: Цифровая грамотность
    // ─────────────────────────────────────────────────────────────

    private fun seedDigitalLiteracy() {
        val course = courseRepository.save(
            Course(
                title = "Цифровая грамотность",
                description = "Узнай как работают компьютеры и как безопасно пользоваться интернетом!",
                imageUrl = "",
                difficulty = Difficulty.BEGINNER,
                ageFrom = 7,
                ageTo = 10,
                orderIndex = 3,
                totalBytesReward = 80
            )
        )

        var idx = 1

        // ── Модуль 1: Мир компьютеров ────────────────────────────────
        val m1 = moduleRepository.save(Module(course = course, title = "Мир компьютеров", description = "Что такое компьютер, программы и файлы", orderIndex = 1))

        val m1l1 = lessonRepository.save(Lesson(module = m1, title = "Что такое компьютер?", lessonType = LessonType.THEORY, orderIndex = idx++, xpReward = 10, bytesReward = 5, content = """
# Что такое компьютер?

## Компьютер — это умная машина!

Компьютер — это устройство, которое умеет:
- Получать информацию (через клавиатуру, мышку, камеру)
- Обрабатывать информацию (думать и считать)
- Хранить информацию (на жёстком диске)
- Выводить результат (на экран, принтер)

## Части компьютера

| Часть | Для чего |
|-------|----------|
| **Процессор** | «Мозг» компьютера, выполняет вычисления |
| **Память (RAM)** | Временное хранилище для запущенных программ |
| **Жёсткий диск** | Постоянное хранилище файлов |
| **Монитор** | Экран для вывода информации |
| **Клавиатура и мышь** | Устройства ввода |

## Что такое программа?

Программа — это набор **инструкций** для компьютера. Браузер, игры, Word — всё это программы!

Программисты пишут эти инструкции на **языках программирования**, например Python.

## Что такое файл?

Файл — это контейнер для хранения информации на компьютере:
- `.txt` — текстовый файл
- `.jpg` — картинка
- `.mp3` — музыка
- `.py` — программа на Python

## Операционная система

Операционная система (Windows, macOS, Linux) — это главная программа, которая управляет всеми остальными программами и устройствами.
        """.trimIndent()))

        saveQuiz(m1l1, "Что является «мозгом» компьютера?", listOf("Процессор" to true, "Монитор" to false, "Клавиатура" to false, "Мышь" to false))
        saveQuiz(m1l1, "Что такое программа?", listOf("Набор инструкций для компьютера" to true, "Часть компьютера" to false, "Файл с картинкой" to false, "Вид кабеля" to false))
        saveQuiz(m1l1, "Для чего нужен жёсткий диск?", listOf("Для постоянного хранения файлов" to true, "Для вычислений" to false, "Для вывода звука" to false, "Для подключения к интернету" to false))
        saveQuiz(m1l1, "Что такое операционная система?", listOf("Главная программа, управляющая компьютером" to true, "Игровая программа" to false, "Вид файла" to false, "Часть монитора" to false))
        saveQuiz(m1l1, "Какое расширение у файла с картинкой?", listOf(".jpg" to true, ".mp3" to false, ".txt" to false, ".exe" to false))

        val m1l2 = lessonRepository.save(Lesson(module = m1, title = "Итоговый квиз о компьютерах", lessonType = LessonType.QUIZ, orderIndex = idx++, xpReward = 15, bytesReward = 8, content = "Проверь знания о компьютерах!"))
        saveQuiz(m1l2, "Что делает RAM?", listOf("Временно хранит данные запущенных программ" to true, "Постоянно хранит файлы" to false, "Выводит изображение" to false, "Подключает к интернету" to false))
        saveQuiz(m1l2, "Чем ввод отличается от вывода?", listOf("Ввод — данные в компьютер, вывод — данные из компьютера" to true, "Они одинаковы" to false, "Вывод быстрее" to false, "Ввод только текст" to false))
        saveQuiz(m1l2, "Что такое расширение файла?", listOf("Часть имени, указывающая тип файла" to true, "Размер файла" to false, "Дата создания" to false, "Папка файла" to false))
        saveQuiz(m1l2, "Какие устройства являются устройствами ввода?", listOf("Клавиатура и мышь" to true, "Монитор и колонки" to false, "Принтер и проектор" to false, "Жёсткий диск" to false))
        saveQuiz(m1l2, "Что общего у Windows, macOS и Linux?", listOf("Все являются операционными системами" to true, "Все созданы Microsoft" to false, "Все бесплатны" to false, "Все только для игр" to false))

        // ── Модуль 2: Безопасность в интернете ──────────────────────
        val m2 = moduleRepository.save(Module(course = course, title = "Безопасность в интернете", description = "Правила СТОП, надёжные пароли, защита личных данных", orderIndex = 2))

        val m2l1 = lessonRepository.save(Lesson(module = m2, title = "Правила безопасности в сети", lessonType = LessonType.THEORY, orderIndex = idx++, xpReward = 10, bytesReward = 5, content = """
# Безопасность в интернете

## Правило СТОП

Когда что-то в интернете кажется подозрительным — вспомни правило **СТОП**:

- **С** — Спроси взрослого, если что-то смущает
- **Т** — Не говори личные данные незнакомцам
- **О** — Остановись перед тем, как нажать на ссылку
- **П** — Поделись с родителями, если что-то случилось

## Что НЕ нужно рассказывать в интернете?

Никогда не сообщай незнакомцам:
- Домашний адрес
- Номер телефона
- Пароли
- Данные банковских карт родителей
- Школу, в которой учишься

## Надёжный пароль

Хороший пароль:
- Длинный (не менее 8 символов)
- Содержит буквы, цифры и знаки
- Не является словом из словаря
- Не содержит имя или день рождения

Плохой пароль: `12345`, `password`, `аня2015`
Хороший пароль: `Tr0mb0n!st2024`

## Признаки опасного сайта

Будь осторожен, если сайт:
- Предлагает «бесплатные» призы
- Просит ввести пароль или номер карты
- Выглядит странно или некрасиво
- Имеет адрес с ошибками (goggle.com вместо google.com)

## Кибербуллинг — это тоже опасно

Если кто-то обижает тебя в интернете — не молчи! Расскажи взрослым. Скриншоты помогут доказать факт.
        """.trimIndent()))

        saveQuiz(m2l1, "Что означает буква «С» в правиле СТОП?", listOf("Спроси взрослого" to true, "Создай пароль" to false, "Сообщи другу" to false, "Скачай программу" to false))
        saveQuiz(m2l1, "Какой пароль более надёжный?", listOf("Tr0mb0n!st2024" to true, "12345" to false, "аня2015" to false, "password" to false))
        saveQuiz(m2l1, "Что нельзя сообщать незнакомцам в интернете?", listOf("Домашний адрес и пароли" to true, "Любимый цвет" to false, "Название страны" to false, "Время года" to false))
        saveQuiz(m2l1, "Что такое кибербуллинг?", listOf("Травля и обиды в интернете" to true, "Вид компьютерной игры" to false, "Программа для защиты" to false, "Тип вируса" to false))
        saveQuiz(m2l1, "Что делать если сайт просит ввести номер карты родителей?", listOf("Закрыть сайт и рассказать взрослым" to true, "Ввести данные" to false, "Позвонить в банк" to false, "Переслать ссылку другу" to false))

        val m2l2 = lessonRepository.save(Lesson(module = m2, title = "Итоговый квиз о безопасности", lessonType = LessonType.QUIZ, orderIndex = idx++, xpReward = 15, bytesReward = 8, content = "Проверь знания о безопасности в сети!"))
        saveQuiz(m2l2, "Минимальная длина надёжного пароля?", listOf("8 символов" to true, "4 символа" to false, "2 символа" to false, "1 символ" to false))
        saveQuiz(m2l2, "Что делать если в интернете кто-то тебя обидел?", listOf("Рассказать взрослым" to true, "Ответить грубостью" to false, "Игнорировать всегда" to false, "Удалить аккаунт" to false))
        saveQuiz(m2l2, "Что значит «Остановись» в правиле СТОП?", listOf("Подумай перед нажатием на ссылку" to true, "Выключи компьютер" to false, "Остановись на сайте" to false, "Не двигай мышь" to false))
        saveQuiz(m2l2, "Какой признак подозрительного сайта?", listOf("Предлагает бесплатные призы" to true, "Есть красивый дизайн" to false, "Содержит новости" to false, "Работает быстро" to false))
        saveQuiz(m2l2, "Можно ли использовать одинаковый пароль везде?", listOf("Нет, это опасно" to true, "Да, так удобнее" to false, "Да, если он длинный" to false, "Да, если никто не знает" to false))

        // ── Модуль 3: Итоговый тест ──────────────────────────────────
        val m3 = moduleRepository.save(Module(course = course, title = "Итоговый тест", description = "Финальная проверка знаний по цифровой грамотности", orderIndex = 3))

        val m3l1 = lessonRepository.save(Lesson(module = m3, title = "Финальный тест: Цифровая грамотность", lessonType = LessonType.QUIZ, orderIndex = idx++, xpReward = 40, bytesReward = 20, content = "Финальный тест по курсу Цифровая грамотность!"))
        saveQuiz(m3l1, "Что такое процессор?", listOf("Мозг компьютера" to true, "Экран компьютера" to false, "Хранилище файлов" to false, "Мышка" to false))
        saveQuiz(m3l1, "Что нужно сделать перед кликом на незнакомую ссылку?", listOf("Остановиться и подумать" to true, "Сразу кликнуть" to false, "Переслать друзьям" to false, "Ничего" to false))
        saveQuiz(m3l1, "Чем хранит файлы компьютер постоянно?", listOf("Жёстким диском" to true, "Процессором" to false, "Монитором" to false, "Мышью" to false))
        saveQuiz(m3l1, "Что такое операционная система?", listOf("Программа управляющая всем компьютером" to true, "Вид вируса" to false, "Файл с данными" to false, "Сайт в интернете" to false))
        saveQuiz(m3l1, "Хороший пароль должен содержать?", listOf("Буквы, цифры и знаки" to true, "Только буквы" to false, "Только цифры" to false, "Имя пользователя" to false))
        saveQuiz(m3l1, "Что означает .mp3?", listOf("Аудиофайл" to true, "Видеофайл" to false, "Текстовый файл" to false, "Программа" to false))
        saveQuiz(m3l1, "Кому нельзя сообщать пароль?", listOf("Никому, даже друзьям" to true, "Только незнакомцам" to false, "Только в интернете" to false, "Только взрослым" to false))
        saveQuiz(m3l1, "Что делать если тебя обидели в интернете?", listOf("Рассказать родителям и сохранить скриншот" to true, "Ответить тем же" to false, "Продолжить общение" to false, "Удалить устройство" to false))
    }

    // ─────────────────────────────────────────────────────────────
    // TEST ACCOUNTS
    // ─────────────────────────────────────────────────────────────

    private fun seedTestAccounts(course1: Course) {
        val student = userRepository.save(User(
            fullName = "Иван Петров", login = "student",
            passwordHash = passwordEncoder.encode("student123"),
            role = UserRole.STUDENT, level = 2, xp = 150, bytesBalance = 75
        ))
        val parent = userRepository.save(User(
            fullName = "Мария Петрова", login = "parent",
            passwordHash = passwordEncoder.encode("parent123"),
            role = UserRole.PARENT
        ))
        student.parent = parent
        userRepository.save(student)

        userCourseEnrollmentRepository.save(UserCourseEnrollment(
            user = student, course = course1,
            progressPercent = 20, enrolledAt = LocalDateTime.now().minusDays(7)
        ))

        val lessons = lessonRepository.findByModuleCourseId(course1.id).sortedBy { it.orderIndex }
        lessons.take(5).forEachIndexed { i, lesson ->
            userProgressRepository.save(UserProgress(
                user = student, lesson = lesson,
                status = ProgressStatus.COMPLETED, score = 100,
                completedAt = LocalDateTime.now().minusDays(7 - i.toLong())
            ))
        }

        val achievements = achievementRepository.findAll()
        achievements.find { it.title == "Первый шаг" }?.let {
            userAchievementRepository.save(UserAchievement(user = student, achievement = it, earnedAt = LocalDateTime.now().minusDays(6)))
        }
        achievements.find { it.title == "Пытливый ум" }?.let {
            userAchievementRepository.save(UserAchievement(user = student, achievement = it, earnedAt = LocalDateTime.now().minusDays(2)))
        }
    }

    // ─────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────

    private fun saveCodeTask(lesson: Lesson, taskTitle: String, description: String, initialCode: String, expectedOutput: String, hint: String?) {
        codeTaskRepository.save(CodeTask(
            lesson = lesson, description = description,
            initialCode = initialCode, expectedOutput = expectedOutput, hint = hint
        ))
    }

    private fun saveQuiz(lesson: Lesson, questionText: String, options: List<Pair<String, Boolean>>) {
        val q = quizQuestionRepository.save(QuizQuestion(
            lesson = lesson, questionText = questionText,
            questionType = QuestionType.SINGLE_CHOICE,
            orderIndex = quizQuestionRepository.findByLessonIdOrderByOrderIndex(lesson.id).size + 1
        ))
        options.forEach { (text, correct) ->
            quizOptionRepository.save(QuizOption(question = q, optionText = text, isCorrect = correct))
        }
    }
}
