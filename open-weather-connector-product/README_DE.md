# OpenWeather Anschluss

#Anschirren die Kraft von frei verfügbares Wetter #Daten für eure dienstlichen
Arbeitsgänge mit OpenWeather! Dieser #Axon Efeu Anschluss implementiert Zugang
zu OpenWeatherMap #Daten setzt und schafft verschiedene Wetter Landkarten
innerhalb dem freien Plan:

* **Gängiges Wetter:** Bekommt das gängige Wetter für irgendwelchen Drehort
  weltumspannend.
* **Fortgeschrittene Prognosen:** Planen vorn mit multi-Tag Prognosen.
* **Fällung:** Sagte vorher die Wahrscheinlichkeit Fällung für die nächsten
  #wenige Tage.
* **Wind:** Wind Festigkeit und obsiegend Richtung kann auch sein prophezeit.

Dieser Anschluss unterstützt du mit eine Demo Ausführung zu heruntersetzen eure
Integration Anstrengung

## Demo

Innerhalb diese Demo, Nutzer können zugreifen umfassend **5-Tag Wetterberichte**
für irgendwelchen Drehort weltumspannend. Folg diese simplen Stufen:

1. **Präzise Drehort Beschreibung:** Genau identifizieren das gewünscht mal
   Drehort betreten den Großstadt Namen und sein #entsprechend Land Code. Für
   Drehorte innerhalb #die Vereinigten Staaten, die Aufnahme von dem staatlichen
   Code ferner frischt die Prognose.
2. **Eingeweihter Suche:** Einfach klicken das **Suche** #zuknöpfen zu
   aktivieren den Rettung Arbeitsgang. Der Anschluss will genügend
   #wiedergewinnen und zeigen ein detailliertes **5-Tag vorhergesagt**
   #Spezifikum zu eurem auserwählten Drehort.

![Demo](images/forecast-weather-demo.png)
![Demo](images/precipitation-chart.png) ![Demo](images/wind-chart.png)

## Einrichtung

### Antrag ID
Das OpenWeatherMap Wetter API ist nicht frei zu benutzen. Indes, dort ist eine
freie Version mit minimal API holt ab Entwicklung Zwecke. Zu benutzen den
Anschluss, #ausgewählt ein passendes API Päckchen via das [OpenWeatherMap API
Entwickler](https://openweathermap.org/api) und generieren ein **API
wesentlich**.

##### Wie zu bekommen ein API wesentlich
1. Anmeldung und gehen zu eure [OpenWeatherMap API Schlüssel
   Seite](https://home.openweathermap.org/api_keys)
2. Füg zu eure API wesentlichen Namen und generieren ihm: ![Melde an
   wesentlich](images/register-api-key.png)
3. API Jetzt wesentlich ist verfügbar: ![Melde an
   wesentlich](images/register-successful.png)

#Nachdem ein **API wesentlich** ist verfügbar, du kannst lagern es in eurem
Projekt in den Variablen.yaml Da das variables "openWeatherConnector.appId":

```
@variables.yaml@ 
```
