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
        val course1 = seedCourse1()
        seedCourse2()
        seedTestAccounts(course1)
    }

    private fun seedAchievements() {
        val achievements = listOf(
            Achievement(title = "Первый шаг", description = "Выполни свой первый урок", iconUrl = "🎯", conditionType = AchievementCondition.LESSONS_COMPLETED, conditionValue = 1),
            Achievement(title = "Пытливый ум", description = "Выполни 5 уроков", iconUrl = "🧠", conditionType = AchievementCondition.LESSONS_COMPLETED, conditionValue = 5),
            Achievement(title = "Знаток", description = "Выполни 10 уроков", iconUrl = "📚", conditionType = AchievementCondition.LESSONS_COMPLETED, conditionValue = 10),
            Achievement(title = "Программист", description = "Заверши свой первый курс", iconUrl = "💻", conditionType = AchievementCondition.COURSE_FINISHED, conditionValue = 1),
            Achievement(title = "Безошибочный", description = "Пройди квиз на 100%", iconUrl = "✨", conditionType = AchievementCondition.QUIZZES_PERFECT, conditionValue = 1),
            Achievement(title = "Кодер", description = "Реши 3 задачи по коду", iconUrl = "⌨️", conditionType = AchievementCondition.CODE_TASKS_COMPLETED, conditionValue = 3),
            Achievement(title = "Богач", description = "Накопи 100 байтиков", iconUrl = "💰", conditionType = AchievementCondition.BYTES_EARNED, conditionValue = 100),
            Achievement(title = "Коллекционер", description = "Получи 3 достижения", iconUrl = "🏅", conditionType = AchievementCondition.ACHIEVEMENTS_COUNT, conditionValue = 3)
        )
        achievementRepository.saveAll(achievements)
    }

    private fun seedCourse1(): Course {
        val course = courseRepository.save(
            Course(
                title = "Python для начинающих",
                description = "Изучи основы программирования на Python — один из самых популярных и простых языков в мире!",
                imageUrl = "/images/python-course.png",
                difficulty = Difficulty.BEGINNER,
                ageFrom = 11,
                ageTo = 15,
                orderIndex = 1,
                totalBytesReward = 100
            )
        )

        // Module 1
        val module1 = moduleRepository.save(
            Module(course = course, title = "Знакомство с Python", description = "Узнай, что такое программирование и сделай первые шаги с Python", orderIndex = 1)
        )

        val lesson1 = lessonRepository.save(
            Lesson(
                module = module1,
                title = "Что такое программирование?",
                lessonType = LessonType.THEORY,
                orderIndex = 1,
                xpReward = 10,
                bytesReward = 5,
                content = """# Что такое программирование?

Программирование — это способ общения с компьютером. Мы пишем **инструкции** на специальном языке, и компьютер их выполняет.

## Зачем учить программирование?
- Создавать игры и приложения
- Автоматизировать скучные задачи
- Понимать как работают технологии вокруг нас

## Что такое Python?
Python — один из самых популярных языков программирования. Его легко читать и писать, поэтому он идеально подходит для начинающих!

```python
print("Привет, мир!")
```

Эта простая строчка выводит текст на экран. Попробуй сам в следующем уроке!"""
            )
        )

        val lesson2 = lessonRepository.save(
            Lesson(
                module = module1,
                title = "Твоя первая программа",
                lessonType = LessonType.PRACTICE,
                orderIndex = 2,
                xpReward = 30,
                bytesReward = 15,
                content = "# Твоя первая программа\n\nНапиши программу, которая выводит `Привет, мир!` на экран.\n\nИспользуй функцию `print()` — она выводит текст в консоль."
            )
        )
        saveCodeTask(lesson2, "Напиши программу, которая выводит 'Привет, мир!'", "# Напиши свой код здесь\n", "Привет, мир!", "Используй print(\"Привет, мир!\")")

        val lesson3 = lessonRepository.save(
            Lesson(
                module = module1,
                title = "Проверь себя: основы",
                lessonType = LessonType.QUIZ,
                orderIndex = 3,
                xpReward = 20,
                bytesReward = 10,
                content = "Ответь на вопросы и проверь свои знания!"
            )
        )
        saveQuizQuestion(lesson3, "Что делает функция print()?", QuestionType.SINGLE_CHOICE, 1,
            listOf("Выводит текст на экран" to true, "Читает файл" to false, "Считает числа" to false, "Рисует картинку" to false))
        saveQuizQuestion(lesson3, "Как называется язык программирования, который мы изучаем?", QuestionType.SINGLE_CHOICE, 2,
            listOf("Python" to true, "Java" to false, "HTML" to false, "Scratch" to false))
        saveQuizQuestion(lesson3, "Что выведет print(2 + 3)?", QuestionType.SINGLE_CHOICE, 3,
            listOf("5" to true, "23" to false, "2 + 3" to false, "Ошибку" to false))

        // Module 2
        val module2 = moduleRepository.save(
            Module(course = course, title = "Переменные и типы данных", description = "Узнай как хранить и использовать данные в программах", orderIndex = 2)
        )

        lessonRepository.save(
            Lesson(
                module = module2,
                title = "Что такое переменные?",
                lessonType = LessonType.THEORY,
                orderIndex = 4,
                xpReward = 10,
                bytesReward = 5,
                content = """# Что такое переменные?

Переменная — это контейнер для хранения данных. Представь её как коробку с именем.

```python
name = "Байтик"
age = 11
height = 1.5
```

## Типы данных
- **str** — строки: `"Привет"`, `'Байтик'`
- **int** — целые числа: `5`, `42`, `-10`
- **float** — дробные числа: `3.14`, `1.5`
- **bool** — логические значения: `True`, `False`

## F-строки — удобный способ вставить переменную в текст

```python
name = "Байтик"
age = 11
print(f"Меня зовут {name}, мне {age} лет")
```

Результат: `Меня зовут Байтик, мне 11 лет`"""
            )
        )

        val lesson5 = lessonRepository.save(
            Lesson(
                module = module2,
                title = "Работа с переменными",
                lessonType = LessonType.PRACTICE,
                orderIndex = 5,
                xpReward = 30,
                bytesReward = 15,
                content = "# Работа с переменными\n\nСоздай переменную `name` со значением `'Байтик'` и выведи приветствие: `Привет, Байтик!`"
            )
        )
        saveCodeTask(lesson5, "Создай переменную name со значением 'Байтик' и выведи 'Привет, Байтик!'",
            "name = \n# Выведи приветствие\n", "Привет, Байтик!", "name = 'Байтик'\nprint(f'Привет, {name}!')")

        val lesson6 = lessonRepository.save(
            Lesson(
                module = module2,
                title = "Проверь себя: переменные",
                lessonType = LessonType.QUIZ,
                orderIndex = 6,
                xpReward = 20,
                bytesReward = 10,
                content = "Проверь свои знания о переменных!"
            )
        )
        saveQuizQuestion(lesson6, "Как создать переменную с именем x и значением 5?", QuestionType.SINGLE_CHOICE, 1,
            listOf("x = 5" to true, "5 = x" to false, "var x = 5" to false, "int x = 5" to false))
        saveQuizQuestion(lesson6, "Что такое f-строка в Python?", QuestionType.SINGLE_CHOICE, 2,
            listOf("Строка с переменными внутри" to true, "Быстрая строка" to false, "Форматированный файл" to false, "Специальный символ" to false))
        saveQuizQuestion(lesson6, "Какой тип данных у значения 3.14?", QuestionType.SINGLE_CHOICE, 3,
            listOf("float" to true, "int" to false, "str" to false, "bool" to false))

        // Module 3
        val module3 = moduleRepository.save(
            Module(course = course, title = "Условные операторы", description = "Научись принимать решения в программах с помощью if/else", orderIndex = 3)
        )

        lessonRepository.save(
            Lesson(
                module = module3,
                title = "Условные операторы if/else",
                lessonType = LessonType.THEORY,
                orderIndex = 7,
                xpReward = 10,
                bytesReward = 5,
                content = """# Условные операторы

Условные операторы позволяют программе принимать решения.

```python
age = 12

if age >= 10:
    print("Ты можешь учиться программированию!")
else:
    print("Ты ещё маленький, но скоро сможешь!")
```

## elif — несколько условий

```python
score = 85

if score >= 90:
    print("Отлично!")
elif score >= 70:
    print("Хорошо!")
else:
    print("Нужно постараться")
```

## Операторы сравнения
- `==` — равно
- `!=` — не равно
- `>` — больше
- `<` — меньше
- `>=` — больше или равно
- `<=` — меньше или равно"""
            )
        )

        val lesson8 = lessonRepository.save(
            Lesson(
                module = module3,
                title = "Практика: условия",
                lessonType = LessonType.PRACTICE,
                orderIndex = 8,
                xpReward = 30,
                bytesReward = 15,
                content = "# Практика: условия\n\nНапиши программу: если число 10 больше 5, выведи 'Да', иначе 'Нет'"
            )
        )
        saveCodeTask(lesson8, "Напиши программу: если число 10 больше 5, выведи 'Да', иначе 'Нет'",
            "# Проверь условие\n", "Да", "if 10 > 5:\n    print('Да')\nelse:\n    print('Нет')")

        val lesson9 = lessonRepository.save(
            Lesson(
                module = module3,
                title = "Проверь себя: условия",
                lessonType = LessonType.QUIZ,
                orderIndex = 9,
                xpReward = 20,
                bytesReward = 10,
                content = "Проверь свои знания о условных операторах!"
            )
        )
        saveQuizQuestion(lesson9, "Какой оператор используется для проверки равенства?", QuestionType.SINGLE_CHOICE, 1,
            listOf("==" to true, "=" to false, "!=" to false, ">=" to false))
        saveQuizQuestion(lesson9, "Что выведет: if 5 > 3: print('Да')?", QuestionType.SINGLE_CHOICE, 2,
            listOf("Да" to true, "Нет" to false, "Ошибку" to false, "Ничего" to false))
        saveQuizQuestion(lesson9, "Для чего используется elif?", QuestionType.SINGLE_CHOICE, 3,
            listOf("Для проверки дополнительных условий" to true, "Для завершения программы" to false, "Для создания цикла" to false, "Для вывода текста" to false))

        val lesson10 = lessonRepository.save(
            Lesson(
                module = module3,
                title = "Мини-проект: Калькулятор",
                lessonType = LessonType.PROJECT,
                orderIndex = 10,
                xpReward = 50,
                bytesReward = 25,
                content = "# Мини-проект: Калькулятор\n\nНапиши программу которая вычисляет и выводит результат 15 + 27"
            )
        )
        saveCodeTask(lesson10, "Напиши калькулятор: вычисли и выведи результат 15 + 27",
            "# Твой калькулятор\n", "42", null)

        // Module 4
        val module4 = moduleRepository.save(
            Module(course = course, title = "Циклы", description = "Научись повторять действия с помощью циклов for и while", orderIndex = 4)
        )

        lessonRepository.save(
            Lesson(
                module = module4,
                title = "Циклы for и while",
                lessonType = LessonType.THEORY,
                orderIndex = 11,
                xpReward = 10,
                bytesReward = 5,
                content = """# Циклы

Цикл позволяет выполнять код несколько раз.

## Цикл for

```python
for i in range(5):
    print(i)
```

Выведет числа от 0 до 4.

```python
for i in range(1, 6):
    print(i)
```

Выведет числа от 1 до 5.

## Цикл while

```python
count = 1
while count <= 5:
    print(count)
    count += 1
```

Цикл while выполняется пока условие истинно.

## range()
- `range(5)` — числа от 0 до 4
- `range(1, 6)` — числа от 1 до 5
- `range(0, 10, 2)` — чётные числа от 0 до 8"""
            )
        )

        val lesson12 = lessonRepository.save(
            Lesson(
                module = module4,
                title = "Практика: циклы",
                lessonType = LessonType.PRACTICE,
                orderIndex = 12,
                xpReward = 30,
                bytesReward = 15,
                content = "# Практика: циклы\n\nВыведи числа от 1 до 5, каждое на новой строке"
            )
        )
        saveCodeTask(lesson12, "Выведи числа от 1 до 5, каждое на новой строке",
            "# Используй цикл\n", "1\n2\n3\n4\n5", "for i in range(1, 6):\n    print(i)")

        val lesson13 = lessonRepository.save(
            Lesson(
                module = module4,
                title = "Проверь себя: циклы",
                lessonType = LessonType.QUIZ,
                orderIndex = 13,
                xpReward = 20,
                bytesReward = 10,
                content = "Проверь свои знания о циклах!"
            )
        )
        saveQuizQuestion(lesson13, "Что делает range(1, 4)?", QuestionType.SINGLE_CHOICE, 1,
            listOf("Создаёт числа 1, 2, 3" to true, "Создаёт числа 1, 2, 3, 4" to false, "Создаёт числа 0, 1, 2, 3" to false, "Создаёт числа 4" to false))
        saveQuizQuestion(lesson13, "Сколько раз выполнится цикл: for i in range(3)?", QuestionType.SINGLE_CHOICE, 2,
            listOf("3" to true, "2" to false, "4" to false, "0" to false))
        saveQuizQuestion(lesson13, "Какой цикл выполняется пока условие истинно?", QuestionType.SINGLE_CHOICE, 3,
            listOf("while" to true, "for" to false, "if" to false, "repeat" to false))

        return course
    }

    private fun seedCourse2(): Course {
        val course = courseRepository.save(
            Course(
                title = "Цифровая грамотность",
                description = "Познакомься с компьютерами и цифровым миром в игровой форме!",
                imageUrl = "/images/digital-course.png",
                difficulty = Difficulty.BEGINNER,
                ageFrom = 7,
                ageTo = 10,
                orderIndex = 2,
                totalBytesReward = 30
            )
        )

        val module1 = moduleRepository.save(
            Module(course = course, title = "Мир компьютеров", description = "Узнай что такое компьютер и как он работает", orderIndex = 1)
        )

        lessonRepository.save(
            Lesson(
                module = module1,
                title = "Что такое компьютер?",
                lessonType = LessonType.THEORY,
                orderIndex = 1,
                xpReward = 10,
                bytesReward = 5,
                content = """# Что такое компьютер?

Компьютер — это умная машина, которая помогает нам учиться, играть и работать!

## Части компьютера

🖥️ **Монитор** — это экран, на котором ты видишь всё что происходит.

⌨️ **Клавиатура** — с её помощью ты печатаешь буквы и цифры.

🖱️ **Мышь** — помогает выбирать предметы на экране.

🔲 **Системный блок** — это "мозг" компьютера, здесь хранятся все данные.

## Что умеет компьютер?
- Помогать учиться
- Запускать игры
- Рисовать картинки
- Воспроизводить музыку и видео
- Общаться с людьми в интернете

Компьютер делает только то, что ему говорят. Чтобы говорить с компьютером, нужно знать специальный язык — **язык программирования**!"""
            )
        )

        val lesson2 = lessonRepository.save(
            Lesson(
                module = module1,
                title = "Проверь себя: компьютеры",
                lessonType = LessonType.QUIZ,
                orderIndex = 2,
                xpReward = 15,
                bytesReward = 8,
                content = "Покажи что ты узнал о компьютерах!"
            )
        )
        saveQuizQuestion(lesson2, "Что такое монитор?", QuestionType.SINGLE_CHOICE, 1,
            listOf("Экран компьютера" to true, "Клавиатура" to false, "Мышь" to false, "Принтер" to false))
        saveQuizQuestion(lesson2, "Что делает мышь?", QuestionType.SINGLE_CHOICE, 2,
            listOf("Помогает выбирать предметы на экране" to true, "Выводит звук" to false, "Печатает буквы" to false, "Хранит данные" to false))
        saveQuizQuestion(lesson2, "Как называется 'мозг' компьютера?", QuestionType.SINGLE_CHOICE, 3,
            listOf("Системный блок" to true, "Монитор" to false, "Клавиатура" to false, "Колонки" to false))

        return course
    }

    private fun seedTestAccounts(course1: Course) {
        val student = userRepository.save(
            User(
                fullName = "Иван Петров",
                login = "student",
                passwordHash = passwordEncoder.encode("student123"),
                role = UserRole.STUDENT,
                level = 2,
                xp = 150,
                bytesBalance = 75
            )
        )

        val parent = userRepository.save(
            User(
                fullName = "Мария Петрова",
                login = "parent",
                passwordHash = passwordEncoder.encode("parent123"),
                role = UserRole.PARENT
            )
        )
        student.parent = parent
        userRepository.save(student)

        // Enroll student in course1
        val enrollment = userCourseEnrollmentRepository.save(
            UserCourseEnrollment(
                user = student,
                course = course1,
                progressPercent = 38,
                enrolledAt = LocalDateTime.now().minusDays(7)
            )
        )

        // Complete first 5 lessons of course1
        val allLessons = lessonRepository.findByModuleCourseId(course1.id).sortedBy { it.orderIndex }
        val lessonsToComplete = allLessons.take(5)
        for (lesson in lessonsToComplete) {
            userProgressRepository.save(
                UserProgress(
                    user = student,
                    lesson = lesson,
                    status = ProgressStatus.COMPLETED,
                    score = 100,
                    completedAt = LocalDateTime.now().minusDays(7 - lessonsToComplete.indexOf(lesson).toLong())
                )
            )
        }

        // Award achievements
        val allAchievements = achievementRepository.findAll()
        val firstStep = allAchievements.find { it.title == "Первый шаг" }
        val curioso = allAchievements.find { it.title == "Пытливый ум" }

        if (firstStep != null) {
            userAchievementRepository.save(
                UserAchievement(user = student, achievement = firstStep, earnedAt = LocalDateTime.now().minusDays(6))
            )
        }
        if (curioso != null) {
            userAchievementRepository.save(
                UserAchievement(user = student, achievement = curioso, earnedAt = LocalDateTime.now().minusDays(2))
            )
        }
    }

    private fun saveCodeTask(lesson: Lesson, description: String, initialCode: String, expectedOutput: String, hint: String?) {
        codeTaskRepository.save(
            CodeTask(
                lesson = lesson,
                description = description,
                initialCode = initialCode,
                expectedOutput = expectedOutput,
                hint = hint
            )
        )
    }

    private fun saveQuizQuestion(
        lesson: Lesson,
        questionText: String,
        questionType: QuestionType,
        orderIndex: Int,
        options: List<Pair<String, Boolean>>
    ) {
        val question = quizQuestionRepository.save(
            QuizQuestion(
                lesson = lesson,
                questionText = questionText,
                questionType = questionType,
                orderIndex = orderIndex
            )
        )
        for ((text, isCorrect) in options) {
            quizOptionRepository.save(
                QuizOption(
                    question = question,
                    optionText = text,
                    isCorrect = isCorrect
                )
            )
        }
    }
}
