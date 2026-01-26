// i18n utility for BizCore
// Handles loading translations from backend and managing current language

class I18n {
  constructor() {
    this.translations = {};
    this.currentLanguage = localStorage.getItem('bizcore_language') || 'en';
    this.listeners = [];
  }

  /**
   * Load translations for a specific language from backend
   */
  async loadTranslations(language) {
    try {
      const response = await fetch(`http://localhost:8091/api/translations/${language}`);
      if (response.ok) {
        this.translations[language] = await response.json();
        this.currentLanguage = language;
        localStorage.setItem('bizcore_language', language);
        this.notifyListeners();
        return true;
      }
      return false;
    } catch (error) {
      console.error('Failed to load translations:', error);
      return false;
    }
  }

  /**
   * Get translation for a key
   * Falls back to key itself if translation not found
   */
  t(key) {
    const translations = this.translations[this.currentLanguage];
    return translations?.[key] || key;
  }

  /**
   * Change current language
   */
  async changeLanguage(language) {
    if (!this.translations[language]) {
      await this.loadTranslations(language);
    } else {
      this.currentLanguage = language;
      localStorage.setItem('bizcore_language', language);
      this.notifyListeners();
    }
  }

  /**
   * Get current language
   */
  getCurrentLanguage() {
    return this.currentLanguage;
  }

  /**
   * Subscribe to language changes
   */
  subscribe(listener) {
    this.listeners.push(listener);
    return () => {
      this.listeners = this.listeners.filter(l => l !== listener);
    };
  }

  /**
   * Notify all listeners of language change
   */
  notifyListeners() {
    this.listeners.forEach(listener => listener(this.currentLanguage));
  }

  /**
   * Get available languages from backend
   */
  async getAvailableLanguages() {
    try {
      const response = await fetch('http://localhost:8091/api/translations/languages');
      if (response.ok) {
        return await response.json();
      }
      return ['en', 'uz', 'ru']; // fallback
    } catch (error) {
      console.error('Failed to load available languages:', error);
      return ['en', 'uz', 'ru']; // fallback
    }
  }
}

// Create singleton instance
const i18n = new I18n();

export default i18n;
