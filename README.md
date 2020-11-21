<a href="https://www.statuscake.com" title="Website Uptime Monitoring"><img src="https://app.statuscake.com/button/index.php?Track=5743970&Days=1&Design=2" /></a>
[![Build Status](https://travis-ci.com/joakimstolen/Eksamen_DevOps_App.svg?token=2oGxk1NA54S8kvu1Qqs9&branch=master)](https://travis-ci.com/joakimstolen/Eksamen_DevOps_App)
# Application for PGR301 Exam
## About
Eksamensoppgave for PGR301 DevOps i skyen på Høyskolen Kristiania. Dette er applikasjons-repositoryet. Infrastrukturen til eksamen kan ses her: https://github.com/joakimstolen/Eksamen_DevOps_Terraform 

## Notes
Applikasjonen bruker:
 * Spring Boot 
 * Kotlin
 * H2 database 
 * RestAssured til tester
 * Logzio til logging
 * InfluxDB for metrikker
 * Travis til å bygge nytt Docker container image hver gang master branch endrer seg. Docker image pushes til Google Cloud Platform sitt Container Registry.  
 
 
 ## Oppsett til Travis-CI og GCP
 1. Lag en Service account med Storage Admin-rolle i GCP
 2. Hent JSON-nøkkel for service accounten og legg i rot-mappe
 3. Krypter nøkkelen med `travis encrypt-file "dittfilnavn.json" --add` (bytt ut "dittfilnavn.json med korrekt navn)
 

## Kjøre applikasjonen
* Start InfluxDB ved å kjøre:
    * 1:  
        * `docker run --rm influxdb:1.0 influxd config > influxdb.conf`
     
    * 2:  
        *   `docker run --name influxdb \
        -p 8083:8083 -p 8086:8086 -p 25826:25826/udp \
        -v $PWD/influxdb:/var/lib/influxdb \
        -v $PWD/influxdb.conf:/etc/influxdb/influxdb.conf:ro \
        -v $PWD/types.db:/usr/share/collectd/types.db:ro \
        influxdb:1.0`
*  Kjør applikasjonen fra EksamenApplication.kt
* Besøk `localhost:8080` eller `https://eksamen-cloud-run-valid-6dls4qicva-uc.a.run.app/`
* For å liste ut alle "kortene": besøk `localhost:8080/allCards` eller `https://eksamen-cloud-run-valid-6dls4qicva-uc.a.run.app/allCards`. Her legges det til 5 kort når applikasjonen kjøres
* For å legge til et kort: bruk f.eks Postman til å sende en POST-request med et nytt "kort" ved å kjøre `localhost/8080/allCards/"valgt navn"` eller `https://eksamen-cloud-run-valid-6dls4qicva-uc.a.run.app/allCards/"valgt navn"`
* Kortet som er lagt til, vil nå listes på `localhost:8080/allCards` eller `https://eksamen-cloud-run-valid-6dls4qicva-uc.a.run.app/allCards`. 
* For å se detaljene til et valgt kort: besøk f.eks `localhost:8080/allCards/Bulbasaur` eller `https://eksamen-cloud-run-valid-6dls4qicva-uc.a.run.app/allCards/Bulbasaur`    


## Setting Keys & Secrets
* `travis encrypt-file "dittfilnavn.json" --add`
* `travis encrypt GCP_PROJECT_ID=eksamen-devops --add`
* `travis encrypt IMAGE=gcr.io/eksamen-devops/eksamen-devops-docker-gcp --add`
    * Merk: I dette prosjektet er GCP_PROJECT_ID og IMAGE satt til denne applikasjonens GCP-navn og container i Container Registry i GCP. For å bruke et annet, endre det til ønskede verdier. 

## Docker 
Docker kjører i et multistage build. 
1. Build Image Stage:  Først kompilerer og bygger den koden, så lager den en "/app" katalog som POM-filen blir kopiert til. Så kjøres ``mvn package``.
2. Production Image Stage: Setter base-imaget til å være AdoptOpenJdk11. WORKDIR settes til "/app". Deretter kopieres JAR-filene fra katalogen til kontaineren. 

## Metrikker
### Gauge
Bruker en gauge til å sjekke hvor mange kort som er lagt til når endpointet "/allCards" kalles på. Når programmet initialiseres vil det legges til 5 standard kort. Disse vil alltid legges til. Legger du til et kort før "/allCards" kalles på vil gaugen øke. 
### Counter
Bruker counter til å telle hvor mange ganger GET kalles på i endpointet "/" og hvor mange ganger POST kalles på i enpointet "/allCards/{name}".
### Timer
Bruker timer til å sjekke hvor lang tid det tar å lage et nytt kort. Timeren brukes altså i POST-metoden createCard. 
### Long Task Timer
Slet litt med implementasjonen for denne, men la til et eksempel med Long task timer i SecondaryController-klassen, der jeg la til en sleep() metode for å late som det tar lenger tid å hente endpointet "/page1". 
### Distribution Summary
La til Distribution Summary i POST-metoden createCards for å sjekke hvor mange kort som lages etter applikasjonen er initialisert.  

## Oppgave-sjekkliste 
- [X] Krav til applikasjonen:  
    - [X] Applikasjonen skal eksponere et REST API og ha en database, gjerne "in memory" for eksempel H2 
    - [X] Applikasjonen skal bygge med Maven eller Gradle
    - [X] Applikasjonen kan skrives i Java eller Kotlin
    - [X] Applikasjonen skal ha tester for REST APIet. For eksempel ved hjelp av RestAssured Dersom noen av testene feiler, skal bygget også feile
- [X] Twelve factor app prinsipper: 
    - [X] III Config. Ingen hemmeligheter eller konfigurasjon i applikasjonen
    - [X] XI Logs. Applikasjonen skal bruke et rammeverk for logging, og logge til standard-out, ikke til filer
- [X] Oppgave 1: Docker
    - [X] Repository skal inneholde en .travis.yml fil som gjør travis i stand til å lage et docker image for hver commit som gjøres til Master branch
    - [X] Container imaget lastes opp til Google Container Registry
    - [X] Docker multi-stage bygg
- [X] Oppgave 2: Metrikker
    - [X] Applikasjonen skal registrere egendefinerte metrics
    - [X] Bruk av følgende type metrics Gauge, Counter, DistributionSummary, Timer, LongTaskTimer
    - [X] Levering av Metrics mot InfluxDB
- [X] Oppgave 3: Logger
    - [X] Bruker Logz.io for innsamling, visualisering og analyse av logger
    - [X] Applikasjonen er konfigurert slik at den både logger lokalt til stdout, og leverer logger til Logz.io
- [X] Oppgave 4: Infrastruktur (Se Infrastruktur repository)
    - [X] Eget repository for Terraformkode
    - [X] Travis pipeline som kan kjøre Terraform deploye infrastruktur til for eksempel Google Cloud
    - [X] En Google Cloud Run applikasjon som skal gjøres tilgjengelig på Internet
    - [X] Cloud run applikasjkonen skal kjøre container image som allerede finnes i Container Registry fra oppgave #1
- [X] Oppgave 5: Overvåkning og varsling (Se Infrastruktur repository)
    - [X] Implementere overvåkning, og varsling i applikasjonen ved hjelp av SAAS tjenesten StatusCake
    - [X] Infrastrukturen skal opprettes ved hjelp av Terraform
- [X]  Oppgave 6: Valgfri IAC (Se Infrastruktur repository)
    - [X] Implementert OpsGenie   
    - [X] Oppretter en eksempel-varslingsgruppe med eksempel-medlemmer, i tillegg til en timeplan for hvilke brukere som skal ha tilsyn  
    - [X] Brukes til varsling   
