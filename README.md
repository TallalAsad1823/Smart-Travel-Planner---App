Smart Travel Planner - Android App
Added Features:
Dynamic Place Fetching: Fetches top 5 tourist attractions for any selected city using Wikipedia API.

Offline Mode (SQLite): Implements local data persistence. Once fetched, data is saved in SQLite and accessible without internet.

Custom ListView Adapter: Displays places with a professional, responsive UI.

Integrated WebView: Opens detailed Wikipedia pages within the app.

Navigation Flow:
Country Selection: User selects a country.

City Selection: User selects a city within that country.

Options Screen: User chooses "Best Places to Visit".

Places Screen: Data is loaded from SQLite (if available) or fetched from API.

WebView: Clicking a place opens its detailed Wiki page.
