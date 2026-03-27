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
        seedCourse3()
        seedCourse4()
        seedCourse5()
        seedCourse6()
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

    private fun seedCourse1(): Course {
        val course = courseRepository.save(
            Course(
                title = "Python для начинающих",
                description = "Изучи основы программирования на Python — один из самых популярных и простых языков в мире!",
                imageUrl = "",
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
                imageUrl = "",
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

    private fun seedCourse3(): Course {
        val course = courseRepository.save(
            Course(
                title = "Алгоритмы и логика",
                description = "Научись мыслить как программист! Решай логические задачки и составляй алгоритмы.",
                imageUrl = "",
                difficulty = Difficulty.BEGINNER,
                ageFrom = 7,
                ageTo = 12,
                orderIndex = 3,
                totalBytesReward = 80
            )
        )

        // Module 1: Что такое алгоритм?
        val module1 = moduleRepository.save(
            Module(course = course, title = "Что такое алгоритм?", description = "Познакомься с понятием алгоритма и научись его составлять", orderIndex = 1)
        )

        lessonRepository.save(
            Lesson(
                module = module1,
                title = "Алгоритмы вокруг нас",
                lessonType = LessonType.THEORY,
                orderIndex = 1,
                xpReward = 10,
                bytesReward = 5,
                content = """# Алгоритмы вокруг нас

**Алгоритм** — это чёткая последовательность шагов, которая помогает решить задачу. Алгоритмы окружают нас повсюду!

## Примеры алгоритмов из жизни

### Приготовление бутерброда
1. Возьми хлеб
2. Намажь маслом
3. Положи начинку
4. Накрой вторым куском хлеба
5. Готово!

### Утренний распорядок дня
1. Проснуться
2. Умыться и почистить зубы
3. Одеться
4. Позавтракать
5. Собрать рюкзак
6. Выйти из дома

## Главные свойства алгоритма

- **Понятность** — каждый шаг должен быть понятен
- **Точность** — нет двусмысленных действий
- **Конечность** — алгоритм обязательно завершается
- **Результативность** — в итоге мы получаем нужный результат

## Что такое блок-схема?

Блок-схема — это способ нарисовать алгоритм с помощью фигур и стрелок:
- **Овал** — начало и конец алгоритма
- **Прямоугольник** — действие
- **Ромб** — условие (да/нет)
- **Стрелки** — переход от шага к шагу

Программисты часто рисуют блок-схемы перед тем, как начать писать код — это помогает продумать решение!"""
            )
        )

        val lesson2 = lessonRepository.save(
            Lesson(
                module = module1,
                title = "Проверь себя: алгоритмы",
                lessonType = LessonType.QUIZ,
                orderIndex = 2,
                xpReward = 20,
                bytesReward = 10,
                content = "Проверь что ты узнал об алгоритмах!"
            )
        )
        saveQuizQuestion(lesson2, "Что такое алгоритм?", QuestionType.SINGLE_CHOICE, 1,
            listOf("Последовательность шагов для решения задачи" to true, "Программа на компьютере" to false, "Математическая формула" to false, "Вид компьютера" to false))
        saveQuizQuestion(lesson2, "Что из этого является алгоритмом?", QuestionType.SINGLE_CHOICE, 2,
            listOf("Рецепт приготовления блинов" to true, "Случайное число" to false, "Цвет яблока" to false, "Высота дерева" to false))
        saveQuizQuestion(lesson2, "Сколько шагов может быть в алгоритме?", QuestionType.SINGLE_CHOICE, 3,
            listOf("Любое количество" to true, "Ровно 10" to false, "Не больше 5" to false, "Только 1" to false))

        val lesson3 = lessonRepository.save(
            Lesson(
                module = module1,
                title = "Первый алгоритм на Python",
                lessonType = LessonType.PRACTICE,
                orderIndex = 3,
                xpReward = 30,
                bytesReward = 15,
                content = "# Первый алгоритм на Python\n\nНапиши программу, которая выводит 3 шага алгоритма приготовления чая.\n\nКаждый шаг — на отдельной строке, без слова \"Шаг\"."
            )
        )
        saveCodeTask(
            lesson3,
            "Выведи 3 шага приготовления чая, каждый на новой строке",
            "# Шаг 1: Вскипятить воду\n# Шаг 2: Положить чайный пакетик в чашку\n# Шаг 3: Залить кипятком\n",
            "Вскипятить воду\nПоложить чайный пакетик в чашку\nЗалить кипятком",
            "print('Вскипятить воду')\nprint('Положить чайный пакетик в чашку')\nprint('Залить кипятком')"
        )

        // Module 2: Логические задачки
        val module2 = moduleRepository.save(
            Module(course = course, title = "Логические задачки", description = "Изучи логику — основу любой программы", orderIndex = 2)
        )

        lessonRepository.save(
            Lesson(
                module = module2,
                title = "Логика: истина и ложь",
                lessonType = LessonType.THEORY,
                orderIndex = 4,
                xpReward = 10,
                bytesReward = 5,
                content = """# Логика: истина и ложь

В программировании есть специальные значения: **True** (истина) и **False** (ложь). Это **булевы значения** — названы в честь математика Джорджа Буля.

## Логические операторы

### and — «и»
Результат истинен только если **оба** условия истинны:

```python
True and True   # True
True and False  # False
False and False # False
```

### or — «или»
Результат истинен если **хотя бы одно** условие истинно:

```python
True or False   # True
False or False  # False
True or True    # True
```

### not — «не»
Меняет значение на противоположное:

```python
not True   # False
not False  # True
```

## Примеры из жизни

```python
hungry = True
has_food = True

if hungry and has_food:
    print("Пора кушать!")
```

```python
is_raining = False
has_umbrella = True

if not is_raining or has_umbrella:
    print("Можно идти гулять!")
```

Логика — это фундамент любой программы. Все решения компьютер принимает именно с помощью True и False!"""
            )
        )

        val lesson5 = lessonRepository.save(
            Lesson(
                module = module2,
                title = "Логические выражения",
                lessonType = LessonType.PRACTICE,
                orderIndex = 5,
                xpReward = 30,
                bytesReward = 15,
                content = "# Логические выражения\n\nПроверь: правда ли что 10 > 5 и 3 < 7? Сохрани результат в переменную `result` и выведи его."
            )
        )
        saveCodeTask(
            lesson5,
            "Проверь: правда ли что 10 > 5 и 3 < 7? Выведи результат.",
            "# Составь логическое выражение\nresult = \nprint(result)\n",
            "True",
            "result = 10 > 5 and 3 < 7\nprint(result)"
        )

        val lesson6 = lessonRepository.save(
            Lesson(
                module = module2,
                title = "Проверь себя: логика",
                lessonType = LessonType.QUIZ,
                orderIndex = 6,
                xpReward = 20,
                bytesReward = 10,
                content = "Проверь свои знания о логических операторах!"
            )
        )
        saveQuizQuestion(lesson6, "Что вернёт True and False?", QuestionType.SINGLE_CHOICE, 1,
            listOf("False" to true, "True" to false, "None" to false, "Error" to false))
        saveQuizQuestion(lesson6, "Что такое оператор not?", QuestionType.SINGLE_CHOICE, 2,
            listOf("Меняет True на False и наоборот" to true, "Складывает числа" to false, "Сравнивает строки" to false, "Создаёт цикл" to false))
        saveQuizQuestion(lesson6, "Что вернёт 5 > 3 or 2 > 10?", QuestionType.SINGLE_CHOICE, 3,
            listOf("True" to true, "False" to false, "Error" to false, "None" to false))

        // Module 3: Линейные алгоритмы
        val module3 = moduleRepository.save(
            Module(course = course, title = "Линейные алгоритмы", description = "Создавай программы с последовательными действиями", orderIndex = 3)
        )

        lessonRepository.save(
            Lesson(
                module = module3,
                title = "Последовательные действия",
                lessonType = LessonType.THEORY,
                orderIndex = 7,
                xpReward = 10,
                bytesReward = 5,
                content = """# Последовательные действия

**Линейный алгоритм** — это набор действий, которые выполняются строго по порядку, одно за другим. Никаких ветвлений, никаких повторений — просто шаг за шагом.

## Пример линейной программы

```python
# Шаг 1: Запросить имя
name = "Байтик"

# Шаг 2: Запросить возраст
age = 11

# Шаг 3: Вывести приветствие
print(f"Привет! Тебя зовут {name}.")
print(f"Тебе {age} лет.")
print("Добро пожаловать на курс!")
```

## Порядок имеет значение!

В линейном алгоритме порядок шагов критически важен. Нельзя вывести переменную до того, как она создана:

```python
# Правильно:
name = "Байтик"
print(name)

# Неправильно — ошибка!
print(name)
name = "Байтик"
```

## Линейные алгоритмы в жизни

Большинство простых программ линейные: запустился, выполнил шаги, завершился. Например, программа перевода температуры из Цельсия в Фаренгейт:

```python
celsius = 100
fahrenheit = celsius * 9 / 5 + 32
print(f"{celsius}°C = {fahrenheit}°F")
```"""
            )
        )

        val lesson8 = lessonRepository.save(
            Lesson(
                module = module3,
                title = "Мини-проект: Визитка",
                lessonType = LessonType.PROJECT,
                orderIndex = 8,
                xpReward = 50,
                bytesReward = 25,
                content = "# Мини-проект: Визитка\n\nСоздай визитку: выведи имя, возраст и хобби с метками на отдельных строках.\n\nФормат: `Имя: Байтик`, `Возраст: 11`, `Хобби: Программирование`"
            )
        )
        saveCodeTask(
            lesson8,
            "Создай визитку: выведи имя, возраст и хобби на отдельных строках",
            "name = 'Байтик'\nage = 11\nhobby = 'Программирование'\n\n# Выведи визитку\n",
            "Имя: Байтик\nВозраст: 11\nХобби: Программирование",
            "print(f'Имя: {name}')\nprint(f'Возраст: {age}')\nprint(f'Хобби: {hobby}')"
        )

        return course
    }

    private fun seedCourse4(): Course {
        val course = courseRepository.save(
            Course(
                title = "Веб-разработка для детей",
                description = "Создай свою первую веб-страницу! Узнай основы HTML и CSS.",
                imageUrl = "",
                difficulty = Difficulty.INTERMEDIATE,
                ageFrom = 12,
                ageTo = 15,
                orderIndex = 4,
                totalBytesReward = 120
            )
        )

        // Module 1: Основы HTML
        val module1 = moduleRepository.save(
            Module(course = course, title = "Основы HTML", description = "Познакомься с языком разметки веб-страниц", orderIndex = 1)
        )

        lessonRepository.save(
            Lesson(
                module = module1,
                title = "Что такое HTML?",
                lessonType = LessonType.THEORY,
                orderIndex = 1,
                xpReward = 10,
                bytesReward = 5,
                content = """# Что такое HTML?

**HTML** (HyperText Markup Language) — это язык разметки, на котором написаны все веб-страницы в интернете. Когда ты открываешь любой сайт, браузер читает HTML и отображает страницу.

## Как устроена HTML-страница?

```html
<!DOCTYPE html>
<html>
  <head>
    <title>Моя страница</title>
  </head>
  <body>
    <h1>Привет, мир!</h1>
    <p>Это моя первая веб-страница.</p>
  </body>
</html>
```

## Основные теги

| Тег | Что делает |
|-----|------------|
| `<html>` | Корневой элемент страницы |
| `<head>` | Служебная информация (не видна на странице) |
| `<title>` | Заголовок вкладки браузера |
| `<body>` | Всё что видит пользователь |
| `<h1>` — `<h6>` | Заголовки (h1 — самый большой) |
| `<p>` | Абзац текста |
| `<br>` | Перенос строки |

## Как работают теги?

Большинство тегов имеют открывающий и закрывающий вариант:

```html
<p>Это текст абзаца</p>
<h1>Это заголовок</h1>
```

Открывающий тег: `<тег>`
Закрывающий тег: `</тег>` (со слэшем)

Браузеры очень терпимы к ошибкам в HTML — даже неправильный код чаще всего отобразится, хотя и не так, как ожидалось."""
            )
        )

        val lesson2 = lessonRepository.save(
            Lesson(
                module = module1,
                title = "Проверь себя: HTML основы",
                lessonType = LessonType.QUIZ,
                orderIndex = 2,
                xpReward = 20,
                bytesReward = 10,
                content = "Проверь свои знания об основах HTML!"
            )
        )
        saveQuizQuestion(lesson2, "Что означает HTML?", QuestionType.SINGLE_CHOICE, 1,
            listOf("HyperText Markup Language" to true, "High Tech Machine Learning" to false, "Home Tool Markup Language" to false, "Hyper Transfer Markup Layer" to false))
        saveQuizQuestion(lesson2, "Какой тег создаёт заголовок?", QuestionType.SINGLE_CHOICE, 2,
            listOf("<h1>" to true, "<p>" to false, "<div>" to false, "<img>" to false))
        saveQuizQuestion(lesson2, "Какой тег создаёт абзац текста?", QuestionType.SINGLE_CHOICE, 3,
            listOf("<p>" to true, "<h1>" to false, "<a>" to false, "<br>" to false))

        lessonRepository.save(
            Lesson(
                module = module1,
                title = "Ссылки и изображения",
                lessonType = LessonType.THEORY,
                orderIndex = 3,
                xpReward = 10,
                bytesReward = 5,
                content = """# Ссылки и изображения

Две важнейшие возможности HTML — добавлять ссылки и изображения.

## Ссылки — тег `<a>`

Тег `<a>` создаёт гиперссылку. Атрибут `href` указывает куда ведёт ссылка:

```html
<a href="https://www.google.com">Перейти в Google</a>
<a href="https://yandex.ru">Открыть Яндекс</a>
```

### Атрибут target

Чтобы ссылка открывалась в новой вкладке, используй `target="_blank"`:

```html
<a href="https://www.google.com" target="_blank">Открыть в новой вкладке</a>
```

## Изображения — тег `<img>`

Тег `<img>` вставляет картинку. Это **самозакрывающийся** тег — у него нет закрывающего тега:

```html
<img src="photo.jpg" alt="Моя фотография">
<img src="logo.png" alt="Логотип сайта" width="200">
```

### Важные атрибуты тега img

| Атрибут | Описание |
|---------|----------|
| `src` | Путь к файлу изображения |
| `alt` | Текст, если картинка не загрузилась (и для незрячих) |
| `width` | Ширина картинки в пикселях |
| `height` | Высота картинки в пикселях |

## Атрибут alt — зачем он нужен?

Атрибут `alt` очень важен:
- Показывается если картинка не загрузилась
- Зачитывается программами для людей с нарушением зрения
- Учитывается поисковыми системами

Всегда добавляй осмысленный `alt` к изображениям!"""
            )
        )

        val lesson4 = lessonRepository.save(
            Lesson(
                module = module1,
                title = "Проверь себя: теги HTML",
                lessonType = LessonType.QUIZ,
                orderIndex = 4,
                xpReward = 20,
                bytesReward = 10,
                content = "Проверь знания о ссылках и изображениях в HTML!"
            )
        )
        saveQuizQuestion(lesson4, "Какой тег создаёт ссылку?", QuestionType.SINGLE_CHOICE, 1,
            listOf("<a>" to true, "<link>" to false, "<url>" to false, "<href>" to false))
        saveQuizQuestion(lesson4, "Какой атрибут у тега <img> задаёт путь к картинке?", QuestionType.SINGLE_CHOICE, 2,
            listOf("src" to true, "href" to false, "alt" to false, "path" to false))
        saveQuizQuestion(lesson4, "Что делает атрибут alt у изображения?", QuestionType.SINGLE_CHOICE, 3,
            listOf("Описание картинки для тех кто не видит" to true, "Задаёт размер" to false, "Добавляет рамку" to false, "Определяет формат" to false))

        // Module 2: Основы CSS
        val module2 = moduleRepository.save(
            Module(course = course, title = "Основы CSS", description = "Научись делать страницы красивыми с помощью стилей", orderIndex = 2)
        )

        lessonRepository.save(
            Lesson(
                module = module2,
                title = "Что такое CSS?",
                lessonType = LessonType.THEORY,
                orderIndex = 5,
                xpReward = 10,
                bytesReward = 5,
                content = """# Что такое CSS?

**CSS** (Cascading Style Sheets — Каскадные таблицы стилей) — это язык, который описывает как должны выглядеть элементы HTML-страницы. Если HTML — это скелет, то CSS — это одежда и внешность.

## Как подключить CSS к HTML?

Самый простой способ — прямо в HTML с помощью тега `<style>`:

```html
<head>
  <style>
    h1 {
      color: blue;
      font-size: 32px;
    }
    p {
      color: gray;
    }
  </style>
</head>
```

Или через отдельный файл `styles.css`:

```html
<head>
  <link rel="stylesheet" href="styles.css">
</head>
```

## Основные свойства CSS

### Текст и шрифты

```css
color: red;              /* цвет текста */
font-size: 18px;         /* размер шрифта */
font-weight: bold;       /* жирный текст */
text-align: center;      /* выравнивание */
```

### Фон и рамки

```css
background-color: yellow;   /* цвет фона */
border: 2px solid black;    /* рамка */
border-radius: 10px;        /* скруглённые углы */
```

### Отступы

```css
margin: 20px;      /* внешний отступ */
padding: 10px;     /* внутренний отступ */
```

## Как CSS выбирает элементы?

```css
/* По тегу — все заголовки h1 */
h1 { color: blue; }

/* По классу — элементы с class="important" */
.important { font-weight: bold; }

/* По id — элемент с id="header" */
#header { background: gray; }
```"""
            )
        )

        val lesson6 = lessonRepository.save(
            Lesson(
                module = module2,
                title = "Проверь себя: CSS",
                lessonType = LessonType.QUIZ,
                orderIndex = 6,
                xpReward = 20,
                bytesReward = 10,
                content = "Проверь свои знания о CSS!"
            )
        )
        saveQuizQuestion(lesson6, "Для чего нужен CSS?", QuestionType.SINGLE_CHOICE, 1,
            listOf("Для оформления внешнего вида страницы" to true, "Для создания структуры" to false, "Для программирования логики" to false, "Для хранения данных" to false))
        saveQuizQuestion(lesson6, "Какое свойство меняет цвет текста?", QuestionType.SINGLE_CHOICE, 2,
            listOf("color" to true, "text-color" to false, "font-color" to false, "background" to false))
        saveQuizQuestion(lesson6, "Какое свойство задаёт размер шрифта?", QuestionType.SINGLE_CHOICE, 3,
            listOf("font-size" to true, "text-size" to false, "size" to false, "font-width" to false))

        lessonRepository.save(
            Lesson(
                module = module2,
                title = "Мини-проект: моя страница",
                lessonType = LessonType.THEORY,
                orderIndex = 7,
                xpReward = 50,
                bytesReward = 25,
                content = """# Мини-проект: Моя первая веб-страница

Поздравляем — ты освоил основы HTML и CSS! Теперь пора создать свою первую настоящую веб-страницу.

## Задание

Скопируй код ниже в файл `index.html`, открой его в браузере и посмотри результат. Затем попробуй изменить текст и цвета на свой вкус!

## Шаблон страницы

```html
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>Моя страница</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f0f8ff;
      margin: 40px;
    }

    h1 {
      color: #2c3e50;
      text-align: center;
    }

    .card {
      background-color: white;
      border-radius: 12px;
      padding: 20px;
      max-width: 500px;
      margin: 0 auto;
      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }

    p {
      color: #555;
      font-size: 16px;
    }

    .highlight {
      color: #e74c3c;
      font-weight: bold;
    }

    a {
      color: #3498db;
    }
  </style>
</head>
<body>
  <h1>Привет! Я Байтик</h1>
  <div class="card">
    <p>Меня зовут <span class="highlight">Байтик</span>, мне 11 лет.</p>
    <p>Я учусь программировать на <span class="highlight">Python</span> и создавать сайты!</p>
    <p>Мои хобби: программирование, чтение книг, игры в шахматы.</p>
    <p>
      <a href="https://bytik.ru">Посетить платформу Байтик</a>
    </p>
  </div>
</body>
</html>
```

## Что попробовать изменить?

1. Поменяй имя и возраст на своё
2. Измени `background-color: #f0f8ff` на другой цвет
3. Добавь ещё один абзац `<p>` со своими интересами
4. Попробуй добавить картинку через `<img src="photo.jpg" alt="Мой фото">`

Сохрани файл и нажми F5 в браузере — изменения появятся сразу!"""
            )
        )

        return course
    }

    private fun seedCourse5(): Course {
        val course = courseRepository.save(
            Course(
                title = "Безопасность в интернете",
                description = "Научись безопасно пользоваться интернетом. Защити себя и свои данные!",
                imageUrl = "",
                difficulty = Difficulty.BEGINNER,
                ageFrom = 7,
                ageTo = 12,
                orderIndex = 5,
                totalBytesReward = 60
            )
        )

        // Module 1: Основы безопасности
        val module1 = moduleRepository.save(
            Module(course = course, title = "Основы безопасности", description = "Узнай главные правила безопасного поведения в интернете", orderIndex = 1)
        )

        lessonRepository.save(
            Lesson(
                module = module1,
                title = "Правила безопасности в интернете",
                lessonType = LessonType.THEORY,
                orderIndex = 1,
                xpReward = 10,
                bytesReward = 5,
                content = """# Правила безопасности в интернете

Интернет — это удивительное место, но там тоже есть опасности. Важно знать правила, чтобы оставаться в безопасности!

## Золотые правила

### Правило 1: Не делись личными данными
Никогда не сообщай незнакомым людям:
- Домашний адрес
- Номер телефона
- Название школы и класс
- Пароли от аккаунтов
- Фотографии без разрешения родителей

### Правило 2: Не открывай подозрительные ссылки
Если получил ссылку от незнакомца — не нажимай! Это может быть:
- Вирус, который сломает компьютер
- Мошенник, который хочет украсть данные
- Неприятный контент

### Правило 3: Не скачивай файлы с неизвестных сайтов
Скачивай программы и файлы только с официальных сайтов. Пиратские сайты часто содержат вирусы!

### Правило 4: Будь осторожен с незнакомцами
Человек в интернете может быть не тем, кем представляется. Не соглашайся встречаться с незнакомцами.

## Правило СТОП

Если что-то в интернете тебя пугает или кажется странным — вспомни правило **СТОП**:

- **С**той — не делай ничего сразу
- **Т**ебе нужно подумать — что происходит?
- **О**братись к взрослому — расскажи маме, папе или учителю
- **П**осоветуйся — взрослые помогут разобраться

## Что делать если что-то случилось?

Никогда не бойся рассказать взрослым! Если тебя кто-то обидел в интернете, прислал что-то неприятное или ты нажал на подозрительную ссылку — сразу скажи родителям. Они помогут и не будут ругать!"""
            )
        )

        val lesson2 = lessonRepository.save(
            Lesson(
                module = module1,
                title = "Проверь себя: безопасность",
                lessonType = LessonType.QUIZ,
                orderIndex = 2,
                xpReward = 20,
                bytesReward = 10,
                content = "Проверь свои знания о безопасности в интернете!"
            )
        )
        saveQuizQuestion(lesson2, "Можно ли делиться адресом дома в интернете?", QuestionType.SINGLE_CHOICE, 1,
            listOf("Нет, это опасно" to true, "Да, если просят" to false, "Только в играх" to false, "Да, всегда" to false))
        saveQuizQuestion(lesson2, "Что делать если незнакомец пишет тебе в интернете?", QuestionType.SINGLE_CHOICE, 2,
            listOf("Рассказать родителям" to true, "Ответить ему" to false, "Дать свой номер" to false, "Отправить фото" to false))
        saveQuizQuestion(lesson2, "Можно ли скачивать файлы с неизвестных сайтов?", QuestionType.SINGLE_CHOICE, 3,
            listOf("Нет, там могут быть вирусы" to true, "Да, всегда" to false, "Только игры" to false, "Да, если бесплатно" to false))
        saveQuizQuestion(lesson2, "Что означает правило СТОП?", QuestionType.SINGLE_CHOICE, 4,
            listOf("Стой, Подумай, Обратись к взрослому, Посоветуйся" to true, "Стой, Тормози, Останавливайся, Прекращай" to false, "Скажи, Только, Один, Пароль" to false, "Спроси, Так, Откуда, Письмо" to false))

        lessonRepository.save(
            Lesson(
                module = module1,
                title = "Надёжные пароли",
                lessonType = LessonType.THEORY,
                orderIndex = 3,
                xpReward = 10,
                bytesReward = 5,
                content = """# Надёжные пароли

Пароль — это твой личный ключ к аккаунту. Если пароль слабый, злоумышленник может его угадать и войти в твой аккаунт!

## Что такое пароль?

Пароль — это секретная комбинация символов, которую знаешь только ты. С паролем ты входишь в игры, социальные сети, электронную почту.

## Признаки плохого пароля

Вот самые популярные пароли — их взламывают за секунды:
- `123456` — просто цифры по порядку
- `password` — само слово «пароль»
- `qwerty` — первые буквы клавиатуры
- `мойкот` или `мамапапа` — простые слова
- Имя питомца: `барсик`, `шарик`
- Дата рождения: `01012010`

## Правила надёжного пароля

✅ **Длинный** — не меньше 10 символов
✅ **Смешанный** — заглавные и строчные буквы
✅ **Цифры** — добавь несколько цифр
✅ **Символы** — `!`, `@`, `#`, `${'$'}`, `%`
✅ **Уникальный** — разный для каждого сайта
✅ **Не связан с тобой** — никаких имён, дат рождения

## Пример создания пароля

Возьми фразу: **«Я люблю программирование в 2024 году!»**
Из первых букв: `ЯлпВ2024г!`

Или комбинация случайных слов: `Кот-Луна-Пицца-7`

## Где хранить пароли?

- Используй менеджер паролей (специальная программа)
- Запиши в надёжном месте (не на бумажке у монитора!)
- Никогда не сохраняй в заметках телефона в открытом виде
- Никому не говори свои пароли — даже друзьям!"""
            )
        )

        // Module 2: Защита данных
        val module2 = moduleRepository.save(
            Module(course = course, title = "Защита данных", description = "Узнай что такое персональные данные и как их защитить", orderIndex = 2)
        )

        lessonRepository.save(
            Lesson(
                module = module2,
                title = "Что такое персональные данные?",
                lessonType = LessonType.THEORY,
                orderIndex = 4,
                xpReward = 10,
                bytesReward = 5,
                content = """# Что такое персональные данные?

**Персональные данные** — это любая информация, по которой можно установить личность человека. Их нужно тщательно защищать!

## Что относится к персональным данным?

### Прямые данные (самые важные!):
- Имя, фамилия, отчество
- Дата рождения
- Домашний адрес
- Номер телефона
- Электронная почта
- Фотографии

### Косвенные данные:
- Название школы и класс
- Имена родителей
- Геолокация (где ты находишься)
- История покупок
- Данные о здоровье

### Цифровые данные:
- Логины и пароли
- Данные банковских карт
- Переписка в мессенджерах

## Почему важно защищать данные?

### Что могут сделать мошенники с твоими данными:

1. **Фишинг** — притворятся другом или организацией, чтобы выманить ещё данные
2. **Кража аккаунтов** — зайдут в твои игры или соцсети
3. **Слежка** — будут знать где ты находишься
4. **Шантаж** — могут угрожать раскрыть личную информацию

## Как защитить свои данные?

- Не заполняй сомнительные анкеты в интернете
- Проверяй, кому даёшь разрешение на доступ к геолокации
- Не фотографируй документы (паспорт, свидетельство о рождении)
- Настрой приватность в социальных сетях
- Регулярно меняй пароли

Помни: то что попало в интернет — остаётся там навсегда!"""
            )
        )

        val lesson5 = lessonRepository.save(
            Lesson(
                module = module2,
                title = "Итоговый тест: безопасность",
                lessonType = LessonType.QUIZ,
                orderIndex = 5,
                xpReward = 20,
                bytesReward = 10,
                content = "Итоговая проверка знаний по безопасности в интернете!"
            )
        )
        saveQuizQuestion(lesson5, "Какой пароль самый надёжный?", QuestionType.SINGLE_CHOICE, 1,
            listOf("Kf3\$hR9!mQ2" to true, "123456" to false, "password" to false, "мойкот" to false))
        saveQuizQuestion(lesson5, "Что такое персональные данные?", QuestionType.SINGLE_CHOICE, 2,
            listOf("Информация, по которой можно тебя найти" to true, "Только имя" to false, "Только пароль" to false, "Только телефон" to false))
        saveQuizQuestion(lesson5, "Что делать если нашёл подозрительную ссылку?", QuestionType.SINGLE_CHOICE, 3,
            listOf("Не нажимать и рассказать взрослым" to true, "Нажать и посмотреть" to false, "Отправить другу" to false, "Скопировать" to false))
        saveQuizQuestion(lesson5, "Можно ли использовать один пароль везде?", QuestionType.SINGLE_CHOICE, 4,
            listOf("Нет, это опасно" to true, "Да, удобно" to false, "Да, если длинный" to false, "Только для игр" to false))

        return course
    }

    private fun seedCourse6(): Course {
        val course = courseRepository.save(
            Course(
                title = "Python: продвинутый уровень",
                description = "Углубись в Python! Списки, функции, словари и работа с файлами.",
                imageUrl = "",
                difficulty = Difficulty.INTERMEDIATE,
                ageFrom = 13,
                ageTo = 15,
                orderIndex = 6,
                totalBytesReward = 150
            )
        )

        // Module 1: Списки
        val module1 = moduleRepository.save(
            Module(course = course, title = "Списки", description = "Изучи один из важнейших типов данных в Python", orderIndex = 1)
        )

        lessonRepository.save(
            Lesson(
                module = module1,
                title = "Что такое списки?",
                lessonType = LessonType.THEORY,
                orderIndex = 1,
                xpReward = 10,
                bytesReward = 5,
                content = """# Что такое списки?

**Список** (list) — это упорядоченная коллекция элементов. В одном списке можно хранить множество значений: числа, строки, даже другие списки!

## Создание списков

```python
# Список строк
fruits = ["яблоко", "банан", "апельсин"]

# Список чисел
numbers = [1, 2, 3, 4, 5]

# Смешанный список
mixed = ["Байтик", 11, True, 3.14]

# Пустой список
empty = []
```

## Индексация

Каждый элемент имеет **индекс** — порядковый номер, начиная с **нуля**:

```python
fruits = ["яблоко", "банан", "апельсин"]
print(fruits[0])  # яблоко
print(fruits[1])  # банан
print(fruits[2])  # апельсин
print(fruits[-1]) # апельсин (с конца)
```

## Полезные функции и методы

```python
fruits = ["яблоко", "банан"]

len(fruits)         # 2 — длина списка
fruits.append("виноград")  # добавить в конец
fruits.pop()        # удалить последний элемент
fruits.pop(0)       # удалить по индексу
fruits.insert(1, "груша")  # вставить на позицию 1
```

## Перебор списка циклом

```python
fruits = ["яблоко", "банан", "апельсин"]
for fruit in fruits:
    print(fruit)
```

Вывод:
```
яблоко
банан
апельсин
```

## Срезы списков

```python
numbers = [1, 2, 3, 4, 5]
print(numbers[1:3])   # [2, 3] — с 1 по 2
print(numbers[:3])    # [1, 2, 3] — первые 3
print(numbers[2:])    # [3, 4, 5] — с 3 до конца
```"""
            )
        )

        val lesson2 = lessonRepository.save(
            Lesson(
                module = module1,
                title = "Работа со списками",
                lessonType = LessonType.PRACTICE,
                orderIndex = 2,
                xpReward = 30,
                bytesReward = 15,
                content = "# Работа со списками\n\nСоздай список из 3 любимых фруктов и выведи каждый на новой строке с помощью цикла `for`."
            )
        )
        saveCodeTask(
            lesson2,
            "Создай список из 3 любимых фруктов и выведи каждый на новой строке",
            "fruits = ['яблоко', 'банан', 'апельсин']\n\n# Выведи каждый фрукт\n",
            "яблоко\nбанан\nапельсин",
            "for fruit in fruits:\n    print(fruit)"
        )

        val lesson3 = lessonRepository.save(
            Lesson(
                module = module1,
                title = "Проверь себя: списки",
                lessonType = LessonType.QUIZ,
                orderIndex = 3,
                xpReward = 20,
                bytesReward = 10,
                content = "Проверь свои знания о списках Python!"
            )
        )
        saveQuizQuestion(lesson3, "Что выведет fruits[0] если fruits = ['яблоко', 'банан']?", QuestionType.SINGLE_CHOICE, 1,
            listOf("яблоко" to true, "банан" to false, "0" to false, "Ошибку" to false))
        saveQuizQuestion(lesson3, "Что делает метод append()?", QuestionType.SINGLE_CHOICE, 2,
            listOf("Добавляет элемент в конец списка" to true, "Удаляет элемент" to false, "Сортирует список" to false, "Считает элементы" to false))
        saveQuizQuestion(lesson3, "Как узнать длину списка?", QuestionType.SINGLE_CHOICE, 3,
            listOf("len(список)" to true, "size(список)" to false, "length(список)" to false, "count(список)" to false))

        // Module 2: Функции
        val module2 = moduleRepository.save(
            Module(course = course, title = "Функции", description = "Научись создавать переиспользуемые блоки кода", orderIndex = 2)
        )

        lessonRepository.save(
            Lesson(
                module = module2,
                title = "Что такое функции?",
                lessonType = LessonType.THEORY,
                orderIndex = 4,
                xpReward = 10,
                bytesReward = 5,
                content = """# Что такое функции?

**Функция** — это именованный блок кода, который можно вызывать много раз. Функции помогают не повторять один и тот же код снова и снова.

## Зачем нужны функции?

Представь что нужно поздороваться с 5 разными людьми. Без функций:

```python
print("Привет, Аня!")
print("Привет, Коля!")
print("Привет, Маша!")
```

С функцией:

```python
def greet(name):
    print(f"Привет, {name}!")

greet("Аня")
greet("Коля")
greet("Маша")
```

## Синтаксис функции

```python
def имя_функции(параметр1, параметр2):
    # тело функции
    return результат
```

- `def` — ключевое слово для создания функции
- `имя_функции` — название (как у переменной)
- `параметры` — входные данные (необязательно)
- `return` — что функция возвращает (необязательно)

## Примеры функций

```python
# Функция без параметров
def say_hello():
    print("Привет!")

say_hello()  # Привет!

# Функция с параметром
def square(n):
    return n * n

result = square(5)
print(result)  # 25

# Функция с несколькими параметрами
def add(a, b):
    return a + b

print(add(3, 4))  # 7
```

## Параметры по умолчанию

```python
def greet(name, greeting="Привет"):
    print(f"{greeting}, {name}!")

greet("Байтик")           # Привет, Байтик!
greet("Байтик", "Здравствуй")  # Здравствуй, Байтик!
```

## Принцип DRY

**DRY** — Don't Repeat Yourself (Не повторяй себя). Если один и тот же код встречается дважды — оформи его как функцию!"""
            )
        )

        val lesson5 = lessonRepository.save(
            Lesson(
                module = module2,
                title = "Создание функций",
                lessonType = LessonType.PRACTICE,
                orderIndex = 5,
                xpReward = 30,
                bytesReward = 15,
                content = "# Создание функций\n\nСоздай функцию `square(n)`, которая возвращает квадрат числа. Выведи результат вызова `square(5)`."
            )
        )
        saveCodeTask(
            lesson5,
            "Создай функцию square(n), которая возвращает квадрат числа. Выведи square(5).",
            "def square(n):\n    # верни квадрат числа\n    pass\n\nprint(square(5))\n",
            "25",
            "def square(n):\n    return n * n"
        )

        val lesson6 = lessonRepository.save(
            Lesson(
                module = module2,
                title = "Проверь себя: функции",
                lessonType = LessonType.QUIZ,
                orderIndex = 6,
                xpReward = 20,
                bytesReward = 10,
                content = "Проверь свои знания о функциях Python!"
            )
        )
        saveQuizQuestion(lesson6, "Какое ключевое слово создаёт функцию в Python?", QuestionType.SINGLE_CHOICE, 1,
            listOf("def" to true, "func" to false, "function" to false, "create" to false))
        saveQuizQuestion(lesson6, "Что делает return?", QuestionType.SINGLE_CHOICE, 2,
            listOf("Возвращает значение из функции" to true, "Печатает текст" to false, "Удаляет функцию" to false, "Создаёт переменную" to false))
        saveQuizQuestion(lesson6, "Можно ли вызвать функцию несколько раз?", QuestionType.SINGLE_CHOICE, 3,
            listOf("Да, сколько угодно" to true, "Нет, только один раз" to false, "Только два раза" to false, "Только в цикле" to false))

        // Module 3: Словари
        val module3 = moduleRepository.save(
            Module(course = course, title = "Словари", description = "Изучи словари — мощный способ хранить связанные данные", orderIndex = 3)
        )

        lessonRepository.save(
            Lesson(
                module = module3,
                title = "Словари в Python",
                lessonType = LessonType.THEORY,
                orderIndex = 7,
                xpReward = 10,
                bytesReward = 5,
                content = """# Словари в Python

**Словарь** (dict) — это коллекция, которая хранит данные в виде пар **ключ: значение**. Как настоящий словарь: слово (ключ) и его значение.

## Создание словаря

```python
# Информация о человеке
person = {
    "name": "Байтик",
    "age": 11,
    "city": "Оренбург"
}

# Пустой словарь
empty = {}
```

## Доступ к данным

```python
person = {"name": "Байтик", "age": 11}

print(person["name"])   # Байтик
print(person["age"])    # 11

# Безопасный доступ (нет ошибки если ключа нет)
print(person.get("city", "Не указан"))  # Не указан
```

## Добавление и изменение

```python
person = {"name": "Байтик"}

person["age"] = 11          # добавить новое поле
person["name"] = "Байтик2"  # изменить существующее
del person["age"]           # удалить поле
```

## Перебор словаря

```python
person = {"name": "Байтик", "age": 11, "city": "Оренбург"}

# Только ключи
for key in person:
    print(key)

# Только значения
for value in person.values():
    print(value)

# Ключи и значения вместе
for key, value in person.items():
    print(f"{key}: {value}")
```

## Полезные методы

```python
d = {"a": 1, "b": 2}

d.keys()    # dict_keys(['a', 'b'])
d.values()  # dict_values([1, 2])
d.items()   # dict_items([('a', 1), ('b', 2)])
len(d)      # 2
"a" in d    # True — проверка наличия ключа
```

## Словари в жизни

Словари идеально подходят для хранения структурированной информации:
- Контакты телефонной книги
- Данные пользователя
- Настройки программы
- Результаты игры"""
            )
        )

        val lesson8 = lessonRepository.save(
            Lesson(
                module = module3,
                title = "Работа со словарями",
                lessonType = LessonType.PRACTICE,
                orderIndex = 8,
                xpReward = 30,
                bytesReward = 15,
                content = "# Работа со словарями\n\nСоздай словарь с информацией о себе (name, age, city) и выведи строку `Меня зовут {name}`."
            )
        )
        saveCodeTask(
            lesson8,
            "Создай словарь с информацией о себе (name, age, city) и выведи 'Меня зовут {name}'",
            "person = {\n    'name': 'Байтик',\n    'age': 11,\n    'city': 'Оренбург'\n}\n\n# Выведи имя\n",
            "Меня зовут Байтик",
            "print(f\"Меня зовут {person['name']}\")"
        )

        val lesson9 = lessonRepository.save(
            Lesson(
                module = module3,
                title = "Мини-проект: Телефонная книга",
                lessonType = LessonType.PROJECT,
                orderIndex = 9,
                xpReward = 50,
                bytesReward = 25,
                content = "# Мини-проект: Телефонная книга\n\nСоздай телефонную книгу — словарь из 3 контактов, где ключ это имя, а значение — номер. Выведи все имена контактов, каждое на новой строке."
            )
        )
        saveCodeTask(
            lesson9,
            "Создай телефонную книгу (словарь) из 3 контактов и выведи все имена",
            "contacts = {\n    'Мама': '+7-900-111-1111',\n    'Папа': '+7-900-222-2222',\n    'Друг': '+7-900-333-3333'\n}\n\n# Выведи имена контактов\n",
            "Мама\nПапа\nДруг",
            "for name in contacts:\n    print(name)"
        )

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
