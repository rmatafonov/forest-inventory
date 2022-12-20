import re


names_dict = {
    'колесная пара': ['колесный параллелопед', 'колесный экипаж', 'колесную пару', 'песня'],
    'рама боковая': ['полет', 'рам боковой', 'рамб боковой', 'раунд угловой', 'храм боковая', 'рамба кавай',
                     'раунд боковой', 'рамб кабай', 'раз буковая', 'рамба кв', 'рамка', 'рома боковая',
                     'храм и боковой', 'раз боковой', 'ромбовый', 'рамблер', 'рамбэка', 'раунд на', 'рамба голая',
                     'раз боковая', 'раунд боковая', 'роман боковой', 'рама боковой', 'храмовый клан',
                     'программа боковая', 'нам боковой', 'боковая']
}


def extract_word_with_number(pos: str, alt_pos: list, word: str) -> str:
    words = pos.split()
    num = None
    ind = words.index(word) if word in words else None
    if ind is None:
        return '0'

    j = alt_pos.index(word) if word in alt_pos else None
    if j is None:
        if ind + 1 < len(words) and words[ind + 1].isnumeric():
            num = words[ind + 1]
        elif ind - 1 >= 0 and words[ind - 1].isnumeric():
            num = words[ind - 1]
        if num is None:
            return '0'
        else:
            return num

    for k in range(j + 1, len(alt_pos)):
        if alt_pos[k].endswith('й') and ind + 1 < len(words) and words[ind + 1].isnumeric():
            return words[ind + 1]

    if alt_pos[j - 1].endswith('й') and ind - 1 >= 0 and words[ind - 1].isnumeric():
        return words[ind - 1]

    return '0'


def extract_pos_number(pos: str, word: str) -> str:
    words = pos.split()
    ind = words.index(word) if word in words else None
    if ind is None or ind >= len(words):
        return ''

    num = ''
    for j in range(ind + 1, len(words)):
        has_num = False
        for c in words[j]:
            if c.isnumeric():
                has_num = True
                num += c
        if not has_num:
            break

    return num


def extract_number_after_name(pos: str, pos_name: str):
    num_start = pos.index(pos_name) + len(pos_name)
    pos_wout_name = pos[num_start:]
    words = pos_wout_name.split()
    num = ''
    for j in range(0, len(words)):
        has_num = False
        for c in words[j]:
            if c.isnumeric():
                has_num = True
                num += c
        if not has_num:
            break

    return num


def enrich_year(year_s: str) -> str:
    year_i = int(year_s)
    if year_i < 30:
        return str(year_i + 2000)
    if year_i < 100:
        return str(year_i + 1900)
    return year_s

class TextAnalyzer:
    @staticmethod
    def analyze(data) -> str:
        text = ''
        alternative_sentences = []
        alternative_sentence = []
        for chunk in data['response']['chunks']:
            for alternative in chunk['alternatives']:
                text += alternative['text'] + ' '
                for word in alternative['words']:
                    word_lower = word['word'].lower()
                    if word_lower.startswith('следующ'):
                        alternative_sentences.append(alternative_sentence)
                        alternative_sentence = []
                    else:
                        alternative_sentence.append(word_lower)
        alternative_sentences.append(alternative_sentence)

        text = text.lower()

        if text.startswith('начало записи '):
            text = text[len('начало записи '):]

        if text.startswith('начало '):
            text = text[len('начало '):]

        splitted = re.split('[Сс]ледующ(.*?) (?:запись)?', text)
        splitted = splitted[0::2]

        result = 'наименование,номер,год,завод,комментарий\n'
        for i, position in enumerate(splitted):
            manufacture_alternatives = ['завод', 'залог', 'зовут', 'зао']
            manufacture_num = None
            for manufacture_alt in manufacture_alternatives:
                manufacture_num = extract_word_with_number(position, alternative_sentences[i], manufacture_alt)
                if manufacture_num != '0':
                    position = position.replace(f'{manufacture_alt} {manufacture_num}', '').replace(
                        f'{manufacture_num} {manufacture_alt}', '')
                    break

            year_alternatives = ['год', 'вот', 'вод', 'код', 'года']
            year = None
            for year_alt in year_alternatives:
                year = extract_word_with_number(position, alternative_sentences[i], year_alt)
                if year != '0':
                    position = position.replace(f'{year_alt} {year}', '').replace(f'{year} {year_alt}', '')
                    break

            year = enrich_year(year)

            number = extract_pos_number(position, 'номер')
            position = position.replace(f'номер {number}', '')

            name = None
            for pname in names_dict:
                if pname in position:
                    name = pname
                    if len(number) == 0:
                        number = extract_number_after_name(position, pname)
                    break

                for alt in names_dict[pname]:
                    if alt in position:
                        name = pname
                        if len(number) == 0:
                            number = extract_number_after_name(position, alt)
                        break

            if int(manufacture_num) > 100:
                tmp = manufacture_num
                manufacture_num = year
                year = tmp

            comment = ''
            if 'китай' in position:
                comment = 'китай'
            if 'брак' in position:
                comment = 'брак'
            if 'шайба' in position:
                comment = 'шайба'

            if name is not None:
                result += f'{name},{number},{year},{manufacture_num},{comment}\n'

        return result
