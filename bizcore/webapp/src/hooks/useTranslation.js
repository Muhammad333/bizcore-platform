import { useState, useEffect } from 'react';
import i18n from '../utils/i18n';

/**
 * React hook for using translations
 *
 * Usage:
 * const { t, language, changeLanguage, availableLanguages } = useTranslation();
 *
 * <h1>{t('users.title')}</h1>
 * <button onClick={() => changeLanguage('uz')}>{t('common.changeLanguage')}</button>
 */
export function useTranslation() {
  const [language, setLanguage] = useState(i18n.getCurrentLanguage());
  const [availableLanguages, setAvailableLanguages] = useState(['en', 'uz', 'ru']);

  useEffect(() => {
    // Load translations for current language
    i18n.loadTranslations(language);

    // Load available languages
    i18n.getAvailableLanguages().then(langs => {
      setAvailableLanguages(langs);
    });

    // Subscribe to language changes
    const unsubscribe = i18n.subscribe((newLanguage) => {
      setLanguage(newLanguage);
    });

    return unsubscribe;
  }, []);

  const t = (key) => i18n.t(key);

  const changeLanguage = async (newLanguage) => {
    await i18n.changeLanguage(newLanguage);
  };

  return {
    t,
    language,
    changeLanguage,
    availableLanguages
  };
}

export default useTranslation;
