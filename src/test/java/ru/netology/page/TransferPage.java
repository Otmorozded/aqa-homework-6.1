package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement amount = $("[data-test-id=amount] input");
    private SelenideElement numberCard = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");

    public TransferPage() {
        $(withText("Пополнение карты")).shouldBe(visible);
    }

    public void moneyTransferFromCardToCard(DataHelper.Card card, int transferSum) {
        String string = String.valueOf(transferSum);
        amount.sendKeys(Keys.CONTROL+"A"+Keys.DELETE);
        amount.setValue(String.valueOf(transferSum));
        numberCard.sendKeys(Keys.CONTROL+"A"+Keys.DELETE);
        numberCard.setValue(card.getNumber());
        transferButton.click();
    }


}
