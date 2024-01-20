import java.util.*;

public class Task1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] productNames = {"Product A", "Product B", "Product C"};   // can add more product
        int[] productPrices = {20, 40, 50};                                // with their prices
        int[] quantities = new int[productNames.length];                   // quantities -> same as length of product names
        boolean[] giftWrapChoices = new boolean[productNames.length];     //print gift wrap choices as many times of length of product name

        // Get user input for product quantities and gift wrapping
        for (int i = 0; i < productNames.length; i++) {
            quantities[i] = getQuantity(scanner, productNames[i]);               //take user input for quantity
            giftWrapChoices[i] = getGiftWrapChoice(scanner, productNames[i]);    //take user input for gift wrap choice
        }
        
        int subtotal = calculateSubtotal(productPrices, quantities);            //Calculate subtotal -> All product quantites * their price
        String discountName = calculateDiscount(subtotal, quantities);
        int discount = getDiscountAmount(discountName, subtotal, quantities, productPrices);
        int giftWrapFeeTotal = calculateGiftWrapFee(productNames, quantities, giftWrapChoices);
        int shippingFee = calculateShippingFee(quantities);
        int total = calculateTotal(subtotal, discount, giftWrapFeeTotal, shippingFee);

        displayReceipt(productNames, quantities, productPrices, subtotal, discountName, discount, giftWrapFeeTotal, shippingFee, total);
    }

    private static int calculateSubtotal(int[] prices, int[] quantities) {
        int subtotal = 0;
        for (int i = 0; i < prices.length; i++) {
            subtotal += prices[i] * quantities[i];
        }
        return subtotal;
    }
    
    private static String calculateDiscount(int subtotal, int[] quantities) {
        String discountName = "No Discount";

        // flat_10_discount
        if (subtotal > 200) {
            discountName = "flat_10_discount";
        }
        //bulk_5_discount
        for(int j = 0; j < quantities.length; j++)
        {
            if(quantities[j] > 10 )
            {
                discountName = "bulk_5_discount";
            }
        }

        // bulk_10_discount
        if (getTotalQuantity(quantities) > 20) {
            discountName = "bulk_10_discount";
        }

        // tiered_50_discount
        for (int i = 0; i < quantities.length; i++) {
            if (quantities[i] > 15 && getTotalQuantity(quantities) > 30) {
                discountName = "tiered_50_discount";
            }
        }

        return discountName;
    }

    private static int getDiscountAmount(String discountName, int subtotal, int[] quantities, int[] prices) {
        int discount = 0;

        switch (discountName) {
            case "flat_10_discount":
                discount = 10;
                break;
            case "bulk_10_discount":
                discount = (int) (subtotal * 0.10);
                break;
            case "bulk_5_discount":
                for (int i = 0; i < quantities.length; i++) {
                    if (quantities[i] > 10) {
                        discount += (int) (quantities[i]*prices[i] * 0.05);
                    }
                }
                break;
            case "tiered_50_discount":
                for (int i = 0; i < quantities.length; i++) {
                    if (quantities[i] > 15 && getTotalQuantity(quantities) > 30) {
                        int discountedQuantity = quantities[i] - 15;
                        discount += (int) (discountedQuantity * prices[i] * 0.50);
                    }
                }
                break;
        }

        return discount;
    }

    private static int getTotalQuantity(int[] quantities) {
        int total = 0;
        for (int quantity : quantities) {
            total += quantity;
        }
        return total;
    }

    private static int calculateGiftWrapFee(String[] productNames, int[] quantities, boolean[] giftWrapChoices) {
        int giftWrapFee = 1;
        int giftWrapFeeTotal = 0;

        for (int i = 0; i < productNames.length; i++) {
            if (giftWrapChoices[i]) {
                giftWrapFeeTotal += quantities[i] * giftWrapFee;
            }
        }

        return giftWrapFeeTotal;
    }

    private static int calculateShippingFee(int[] quantities) {
        int itemsPerPackage = 10;
        int shippingFeePerPackage = 5;
        int totalItems = getTotalQuantity(quantities);

        return (int) Math.ceil((double) totalItems / itemsPerPackage) * shippingFeePerPackage;
    }

    private static int calculateTotal(int subtotal, int discount, int giftWrapFeeTotal, int shippingFee) {
        return subtotal - discount + giftWrapFeeTotal + shippingFee;
    }

    private static void displayReceipt(String[] productNames, int[] quantities, int[] prices, int subtotal, String discountName,
                                       int discount, int giftWrapFeeTotal, int shippingFee, int total) {
        System.out.println("\nProduct Details:");
        for (int i = 0; i < productNames.length; i++) {
            System.out.println(productNames[i] + ": Quantity - " + quantities[i] + ", Total - $" + (quantities[i] * prices[i]));
        }

        System.out.println("\nSubtotal: $" + subtotal);
        System.out.println("Discount Applied: " + discountName + " ($" + discount + ")");
        System.out.println("Gift Wrap Fee: $" + giftWrapFeeTotal);
        System.out.println("Shipping Fee: $" + shippingFee);
        System.out.println("\nTotal:  $" + total);
    }
//---------------------------------------------------------- Edge Cases---------------------------------------------------
    private static int getQuantity(Scanner scanner, String productName) {
        int quantity;
        do {
            System.out.print("Enter quantity for " + productName + ": ");
            while (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid numeric quantity.");
                scanner.next();
            }
            quantity = scanner.nextInt();
            if (quantity < 0) {
                System.out.println("Please enter a non-negative quantity.");
            }
        } while (quantity < 0);
        return quantity;
    }

    private static boolean getGiftWrapChoice(Scanner scanner, String productName) {
        String choice;
        do {
            System.out.print("Wrap " + productName + " as a gift? (yes/no): ");
            choice = scanner.next().toLowerCase();
            if (!choice.equals("yes") && !choice.equals("no")) {
                System.out.println("Please enter either 'yes' or 'no'.");
            }
        } while (!choice.equals("yes") && !choice.equals("no"));
        return choice.equals("yes");
    }
}
