from item import Item
from errors import ItemAlreadyExistsError, ItemNotExistError

class ShoppingCart:
    def __init__(self):
        self.items = {}  

    def add_item(self, item: Item):
        if item.name in self.items:
            raise ItemAlreadyExistsError(f"Item '{item.name}' already exists in the cart.")
        self.items[item.name] = item

    def remove_item(self, item_name: str):
        if item_name not in self.items:
            raise ItemNotExistError(f"Item '{item_name}' does not exist in the cart.")
        del self.items[item_name]

    def get_subtotal(self) -> int:
        return sum(item.price for item in self.items.values())
