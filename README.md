# "ChapterHouse" Sistēmas Ceļvedis
----
## Ievads

"ChapterHouse" ir sistēma, kurā pievieno grāmatas no dotā kataloga katram lietotājam atsevišķā sarakstā. Sistēmas mērķis ir dot iespēju lietotājiem izveidot personīgu lasāmo grāmatu sarakstu, kuru ir iespējams filtrēt un kārtot pēc pieprasījuma, kā arī mainīt to lasīšanas statusu.

## Lietotāja Interfeisa Apraksts
----
### Galvenais Sākuma Logs
![Title Page](guide_img/start.png)

Šis ir sākuma izvēlnes logs, kurā ir iespejams reģistrēties, pieslēgties vai atslēgties no sistēmas.

### Sarakstu Izvēlnes Logs
![List Choice](guide_img/listChoice.png)

Sarakstu izvēlnes logs pieejams lietotājam, kas pievienojies, un kurā izvēlas redzēt visas pieejamās grāmatas vai savu izveidoto grāmatu sarakstu.

### Kataloga Izvēlne
![Catalouge Top](guide_img/main_allBooks_top.png)
![Catalouge Bottom](guide_img/main_allBooks.png)

Dotās grāmatas ir iespējams kārtot, filtrēt, meklēt pēc lietotātja ievadītā, pievienot pie lietotāja personīgā saraksta vai iziet ārpus šīs izvēlnes.

### Lietotāja Saraksta Izvēlne
![Reader List](guide_img/main_user_list.png)

Šeit iespējams apskatīt lietotāja pievienotās grāmatas un izpildīt dažādas funkcijas, piemēram, kā kārtošana, meklēšana, filtrēšana, noņemšana, atiestatītšana (reset), mainīt statusu un atgriezties uz ieprikšējo logu.

## Funkciju Apraksti
----
### Pieslēgšanās
Katra izvēle veic dažādas pārbaudes, bet izskats un pieprasījumi ir līdzīgi.
#### 1. Ievada vienu no dotajām izvēlēm (login vai register).
![Start Option](guide_img/functions/login/start_login.png)
#### 2. Ievada lietotājvārdu un e-pastu.
![Start Option](guide_img/functions/login/start_login_user.png)

### Kārtošana
Tā ir pieejama gan katalogam, gan lietotāja sarakstam.
#### 1. Ievada pēc kuras kategorijas un virziena.
![Sorting](guide_img/functions/sort/sorting.png)
#### 2. Izmaiņas ir redzamas saraksta secībā, iekrāsotā virsraksta daļa un saraksta beigās pēc iezīmētā virziena.
![Descriptor Change](guide_img/functions/sort/sorting_top_change.png)
![Direction Change](guide_img/functions/sort/sorting_direction.png)

### Filtrēšana pēc žanra
Pieejams katalogam un lietotāja sarakstam.
#### 1. Izvēlas vai visi žanri tiek pievienoti, vai viens žanrs tiek noņemts vai pievienots. Šajā gadījumā noņems vienu.
![Filter](guide_img/functions/filter_genre/filter.png)
#### 2. Izvēlas, kuru žanru.
![Genre Choice](guide_img/functions/filter_genre/filter_genre.png)
#### 3. Izmaiņas ievērojamas gan pašā sarakstā, gan saraksta beigās, kur ir redzami aktīvie un neaktīvie žanri. Un tiek turpināta izvēle par filtrēšanu līdz lietotājs iziet.
![Genre Result](guide_img/functions/filter_genre/filter_cont_choice.png)

### Grāmatas pievienošana sarakstam
Pieejams tikai katalogam.
#### 1. Lietotājam jāievada ID, kas ir pieejams.
![Add Book](guide_img/functions/add/add_book.png)
#### 2. Atkarībā vai grāmata jau atrodas sarakstā tiek dota ziņa.
![List Inlcudes](guide_img/functions/add/add_negative.png)
![Book Added](guide_img/functions/add/add_positive.png)

### Meklēšana
#### 1. Izvēlas kāda veida meklēšana.
![Search Choice](guide_img/functions/search/search_what.png)
#### 2. Ievada meklējamo.
![Search Input](guide_img/functions/search/search_input.png)
#### 3. Tālāk tiek dota izvēlne ar ko rikojas ar sameklētajām grāmatām.
##### Kataloga izvēlne pēc meklēšanas
![Search Menu Catalogue](guide_img/functions/search/search_menu.png)
##### Lietotāja saraksta izvēlne pēc meklēšanas
![Search Menu Reading List](guide_img/functions/search/search_menu_readingList.png)

### Gramātu noņemšana no saraksta
#### 1. Lietotājam jāievada viens no pieejamajiem ID.
![Remove Book](guide_img/functions/remove/remove_id.png)
#### 2. Grāmata tiek noņemta no saraksta.
![Remove Result](guide_img/functions/remove/remove_result.png)

### Statusa mainīšana
Tikai lietotāja lasīšanas sarakstam.
#### 1. Lietotājam jāievada pieejams ID.
![Change Status ID](guide_img/functions/status_change/status_change_id.png)
#### 2. Redzamās izmaiņas statusa marķējumos.
![Change Status Result](guide_img/functions/status_change/status_change_id_after.png)

### Filtrēšana pēc statusa
Tikai lietotāja lasīšanas sarakstam.
#### 1. Izvēlas vai rādīt visas, vai tikai lasītās, vai tikai nelasītās
![Filter Status](guide_img/functions/filter_status/filter_status.png)
#### 2. Redzamas tikai izvēlētā statusa grāmatas.
![Filter Status Result](guide_img/functions/filter_status/filter_status_result.png)

### Atiestatīšana (reset) - noņem visas filtrēšanas veidus un atgreiž kārtošanas veidu, kurš ir pēc noklušejuma
Tikai lietotāja lasīšanas sarakstam.
#### Pirms atiestatīšanas 
![Before Reset](guide_img/functions/reset/before_reset.png)
#### Pēc atiestatīšanas
![After Reset](guide_img/functions/reset/after_reset.png)