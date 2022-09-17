package ru.fedresurs;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class ParTest {

    @ValueSource(strings = {"6661000096","Уралэлектромонтаж"})
    @ParameterizedTest (name= "При запросе и ИНН и наименования в выдаче должно содержаться юрлицо 'УралЭм'")

    void paramTest(String testData) {
        open("https://fedresurs.ru/");
        $(byName("searchString")).setValue(testData);
        $(byClassName("btn")).click();
        $(byClassName("search-result")).shouldHave(text("УралЭм"));
    }

    @CsvSource(value = {
            "6661000096,  ОАО \"УРАЛЭМ\"",
            "1027739244741,  ООО \"ИНТЕРНЕТ РЕШЕНИЯ\"",
    })
    @ParameterizedTest(name = "Результаты поиска содержат текст \"{1}\" для запроса: \"{0}\"")
    void commonComplexSearchTest(String testData, String expectedResult) {
        open("https://fedresurs.ru/");
        $(byName("searchString")).setValue(testData);
        $(byClassName("btn")).click();
        $$(byClassName("td_name")).get(1).shouldHave(text(expectedResult));
    }

    static Stream<Arguments> dataProviderForSelenideSiteMenuTest() {
        return Stream.of(
                Arguments.of("6661000096", List.of("Наименование","ОАО \"УРАЛЭМ\" 620050, ОБЛАСТЬ СВЕРДЛОВСКАЯ, Г. ЕКАТЕРИНБУРГ, УЛ. МАНЕВРОВАЯ, Д.43")),
                Arguments.of("1027739244741", List.of("Наименование","ООО \"ИНТЕРНЕТ РЕШЕНИЯ\" 123112, ГОРОД МОСКВА, НАБ. ПРЕСНЕНСКАЯ, Д. 10, ПОМЕЩ. I ЭТ 41 КОМН 6"))
        );
    }

    @MethodSource("dataProviderForSelenideSiteMenuTest")
    @ParameterizedTest(name = "Для запроса {0} отображаются данные {1}")
    void selenideSiteMenuTest(String testData, List<String> expectedResult) {
        open("https://fedresurs.ru/");
        $(byName("searchString")).setValue(testData);
        $(byClassName("btn")).click();
        $$(byClassName("td_name")).shouldHave(CollectionCondition.texts(expectedResult));

}}



