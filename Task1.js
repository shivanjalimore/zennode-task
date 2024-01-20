let productNames = ["Product A", "Product B", "Product C"];
let productPrices = [20, 40, 50];
let quantities = [];
let giftWrapChoices = [];

for (let i = 0; i < productNames.length; i++) {
  quantities[i] = getQuantity(productNames[i]);
  giftWrapChoices[i] = getGiftWrapChoice(productNames[i]);
}

let subtotal = calculateSubtotal(productPrices, quantities);
let discountName = calculateDiscount(subtotal, quantities);
let discount = getDiscountAmount(discountName,subtotal,quantities,productPrices);
let giftWrapFeeTotal = calculateGiftWrapFee(productNames,quantities,giftWrapChoices);
let shippingFee = calculateShippingFee(quantities);
let total = calculateTotal(subtotal, discount, giftWrapFeeTotal, shippingFee);

displayReceipt(productNames, quantities, productPrices, subtotal, discountName, discount, giftWrapFeeTotal, shippingFee, total);
function calculateSubtotal(prices, quantities) {
  let subtotal = 0;
  for (let i = 0; i < prices.length; i++) {
    subtotal += prices[i] * quantities[i];
  }
  return subtotal;
}

function calculateDiscount(subtotal, quantities) {
  let discountName = "No Discount";

  if (subtotal > 200) {
    discountName = "flat_10_discount";
  }

  for (let j = 0; j < quantities.length; j++) {
    if (quantities[j] > 10) {
      discountName = "bulk_5_discount";
    }
  }

  if (getTotalQuantity(quantities) > 20) {
    discountName = "bulk_10_discount";
  }

  for (let i = 0; i < quantities.length; i++) {
    if (quantities[i] > 15 && getTotalQuantity(quantities) > 30) {
      discountName = "tiered_50_discount";
    }
  }

  return discountName;
}

function getDiscountAmount(discountName, subtotal, quantities, prices) {
  let discount = 0;

  switch (discountName) {
    case "flat_10_discount":
      discount = 10;
      break;
    case "bulk_10_discount":
      discount = Math.round(subtotal * 0.1);
      break;
    case "bulk_5_discount":
      for (let i = 0; i < quantities.length; i++) {
        if (quantities[i] > 10) {
          discount += Math.round(quantities[i] * prices[i] * 0.05);
        }
      }
      break;
    case "tiered_50_discount":
      for (let i = 0; i < quantities.length; i++) {
        if (quantities[i] > 15 && getTotalQuantity(quantities) > 30) {
          let discountedQuantity = quantities[i] - 15;
          discount += Math.round(discountedQuantity * prices[i] * 0.5);
        }
      }
      break;
  }

  return discount;
}

function getTotalQuantity(quantities) {
  let total = 0;
  for (let quantity of quantities) {
    total += quantity;
  }
  return total;
}

function calculateGiftWrapFee(productNames, quantities, giftWrapChoices) {
  let giftWrapFee = 1;
  let giftWrapFeeTotal = 0;

  for (let i = 0; i < productNames.length; i++) {
    if (giftWrapChoices[i]) {
      giftWrapFeeTotal += quantities[i] * giftWrapFee;
    }
  }

  return giftWrapFeeTotal;
}

function calculateShippingFee(quantities) {
  let itemsPerPackage = 10;
  let shippingFeePerPackage = 5;
  let totalItems = getTotalQuantity(quantities);

  return Math.ceil(totalItems / itemsPerPackage) * shippingFeePerPackage;
}

function calculateTotal(subtotal, discount, giftWrapFeeTotal, shippingFee) {
  return subtotal - discount + giftWrapFeeTotal + shippingFee;
}

function displayReceipt(productNames,quantities,prices,subtotal,discountName,discount,giftWrapFeeTotal,shippingFee,total) 
{
  console.log("\nProduct Details:");
  for (let i = 0; i < productNames.length; i++) {
    console.log(`${productNames[i]}: Quantity - ${quantities[i]}, Total - $${ quantities[i] * prices[i]}` );
  }
  console.log("\nSubtotal: $" + subtotal);
  console.log(`Discount Applied: ${discountName} ($${discount})`);
  console.log("Gift Wrap Fee: $" + giftWrapFeeTotal);
  console.log("Shipping Fee: $" + shippingFee);
  console.log("\nTotal:  $" + total);
}

//----------------------------------------------------------- Edge Cases-----------------------------------------------------
function getQuantity(productName) {
  let quantity;
  do {
    quantity = parseInt(prompt(`Enter quantity for ${productName}: `));
    if (isNaN(quantity) || quantity < 0) {
      console.log("Please enter a valid non-negative numeric quantity.");
    }
  } while (isNaN(quantity) || quantity < 0);
  return quantity;
}

function getGiftWrapChoice(productName) {
  let choice;
  do {
    choice = prompt(`Wrap ${productName} as a gift? (yes/no): `).toLowerCase();
    if (choice !== "yes" && choice !== "no") {
      console.log("Please enter either 'yes' or 'no'.");
    }
  } while (choice !== "yes" && choice !== "no");
  return choice === "yes";
}
