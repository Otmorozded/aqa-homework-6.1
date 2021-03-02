package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private SelenideElement firstCard = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0']");
    private SelenideElement secondCard = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d']");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private String firstCardNumber = "5559 0000 0000 0001";
    private String secondCardNumber = "5559 0000 0000 0002";

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public TransferPage selectCard(String numberCard) {
        if (numberCard == firstCardNumber) {
            firstCard.$(".button").click();
        }
        if (numberCard == secondCardNumber) {
            secondCard.$(".button").click();
        }
        return new TransferPage();
    }

    public int getBalanceCard(String numberCard) {
        String text = "";
        if (numberCard == firstCardNumber) {
            text = firstCard.getText();
        }
        if (numberCard == secondCardNumber) {
            text = secondCard.getText();
        }
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public void refreshSUT() {
        int balanceFirstCard = this.getBalanceCard(firstCardNumber);
        int balanceSecondCard = this.getBalanceCard(secondCardNumber);
        if (balanceFirstCard == balanceSecondCard) {
            return;
        }
        if (balanceFirstCard > balanceSecondCard) {
            int difference = (balanceFirstCard - balanceSecondCard) / 2;
            val transferPage = this.selectCard(secondCardNumber);
            val dataCardFirst = DataHelper.getFirstCard();
            transferPage.moneyTransferFromCardToCard(dataCardFirst, difference);
        }
        if (balanceFirstCard < balanceSecondCard) {
            int difference = (balanceSecondCard - balanceFirstCard) / 2;
            val transferPage = this.selectCard(firstCardNumber);
            val dataCardSecond = DataHelper.getSecondCard();
            transferPage.moneyTransferFromCardToCard(dataCardSecond, difference);
        }
    }
}
