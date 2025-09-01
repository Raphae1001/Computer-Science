import yaml
from item import Item
from shopping_cart import ShoppingCart
from errors import ItemNotExistError, TooManyMatchesError, ItemAlreadyExistsError

class Store:
    def __init__(self, path):
        with open(path) as inventory:
            items_raw = yaml.load(inventory, Loader=yaml.FullLoader)['items']
        self._items = self._convert_to_item_objects(items_raw)
        self._shopping_cart = ShoppingCart()

    @staticmethod
    def _convert_to_item_objects(items_raw):
        return [Item(item['name'],
                     int(item['price']),
                     item['hashtags'],
                     item['description'])
                for item in items_raw]

    def get_items(self) -> list:
        return self._items

    def search_by_name(self, item_name: str) -> list:
        cart_items = {item.name for item in self._shopping_cart.items.values()}
        filtered_items = [item for item in self._items if item_name.lower() in item.name.lower() and item.name not in cart_items]
        filtered_items.sort(key=lambda x: x.name)  # Tri strictement lexical
        return filtered_items

    def search_by_hashtag(self, hashtag: str) -> list:
        cart_items = {item.name for item in self._shopping_cart.items.values()}
        filtered_items = [item for item in self._items if hashtag.lower() in (tag.lower() for tag in item.hashtags) and item.name not in cart_items]
        filtered_items.sort(key=lambda x: x.name)
        return filtered_items

    def add_item(self, item_name: str):
        matching_items = [item for item in self._items if item_name.lower() in item.name.lower()]
        if not matching_items:
            raise ItemNotExistError(f"No item matching '{item_name}' found.")
        if len(matching_items) > 1:
            raise TooManyMatchesError(f"Multiple items matching '{item_name}' found.")
        item = matching_items[0]
        try:
            self._shopping_cart.add_item(item)
        except ItemAlreadyExistsError:
            raise ItemAlreadyExistsError(f"Item '{item.name}' is already in the shopping cart.")

    def remove_item(self, item_name: str):
        matching_items = [item for item in self._items if item_name.lower() in item.name.lower()]
        if not matching_items:
            raise ItemNotExistError(f"Item '{item_name}' does not exist.")
        if len(matching_items) > 1:
            raise TooManyMatchesError(f"Multiple items matching '{item_name}' found.")
        self._shopping_cart.remove_item(matching_items[0].name)

    def checkout(self) -> int:
        return self._shopping_cart.get_subtotal()
