import { useState, useEffect, useMemo, useCallback } from "react";

// ─── CURATED KEYSTONE HABITS (50 elite habits) ───────────────────────────────
const ELITE_HABITS = [
  // BODY
  { name: "Wake by 5:30 AM", category: "Body", icon: "🌅", difficulty: "Hard", time: "Morning", keystone: true, science: "Gains 2–3 hrs of uncontested focus daily. Used by 90% of Fortune 500 CEOs." },
  { name: "Exercise 30+ minutes", category: "Body", icon: "🏋️", difficulty: "Medium", time: "Morning", keystone: true, science: "Raises BDNF — the brain's growth factor. Equivalent to an antidepressant in clinical trials." },
  { name: "Drink 2L of water", category: "Body", icon: "💧", difficulty: "Easy", time: "Anytime", keystone: false, science: "Even 1% dehydration impairs cognitive performance by up to 12%." },
  { name: "Cold shower (2 min)", category: "Body", icon: "🚿", difficulty: "Hard", time: "Morning", keystone: true, science: "Raises dopamine 250% above baseline for up to 3 hours. Builds stress resilience." },
  { name: "Sleep 7–8 hours", category: "Body", icon: "😴", difficulty: "Medium", time: "Evening", keystone: true, science: "Sleep deprivation mimics being legally drunk. Memory consolidation happens entirely during sleep." },
  { name: "Walk 10,000 steps", category: "Body", icon: "🚶", difficulty: "Medium", time: "Anytime", keystone: false, science: "Reduces all-cause mortality risk by 31%. Enhances creative thinking via bilateral movement." },
  { name: "Stretch or yoga (10 min)", category: "Body", icon: "🧘", difficulty: "Easy", time: "Morning", keystone: false, science: "Activates the parasympathetic nervous system, shifting the brain from stress to growth mode." },
  { name: "No alcohol", category: "Body", icon: "🚫", difficulty: "Hard", time: "Anytime", keystone: false, science: "Even one drink disrupts REM sleep by 24%. Compounding clarity compounds weekly." },
  { name: "Eat clean meals (3x)", category: "Body", icon: "🥗", difficulty: "Medium", time: "Anytime", keystone: false, science: "Blood glucose stability directly correlates with mood stability and decision quality." },
  { name: "No screens 1hr before bed", category: "Body", icon: "📵", difficulty: "Medium", time: "Evening", keystone: false, science: "Blue light suppresses melatonin by 50%, delaying sleep onset by 90+ minutes on average." },

  // MIND
  { name: "Meditate (10–20 min)", category: "Mind", icon: "🧠", difficulty: "Medium", time: "Morning", keystone: true, science: "8 weeks of daily meditation measurably thickens the prefrontal cortex — the CEO of your brain." },
  { name: "Gratitude journal (3 items)", category: "Mind", icon: "🙏", difficulty: "Easy", time: "Morning", keystone: true, science: "Rewires the reticular activating system to scan for opportunity over threat. Joy is trainable." },
  { name: "Evening journaling", category: "Mind", icon: "📓", difficulty: "Easy", time: "Evening", keystone: false, science: "Externalizing thoughts reduces cognitive load. Used by Darwin, Einstein, and Da Vinci." },
  { name: "Positive affirmations", category: "Mind", icon: "✨", difficulty: "Easy", time: "Morning", keystone: false, science: "Self-affirmation activates reward centers and reduces defensiveness to performance feedback." },
  { name: "Visualize your goals (5 min)", category: "Mind", icon: "🎯", difficulty: "Easy", time: "Morning", keystone: false, science: "Mental rehearsal activates the same motor neurons as physical practice. Used by Olympic athletes." },
  { name: "Digital detox (1hr)", category: "Mind", icon: "🌿", difficulty: "Hard", time: "Anytime", keystone: false, science: "Social media activates the same dopamine pathways as slot machines. Recovery is exponential." },
  { name: "Deep breathing (4-7-8)", category: "Mind", icon: "🫁", difficulty: "Easy", time: "Anytime", keystone: false, science: "Activates the vagus nerve. Physiological sigh is the fastest known stress-reduction technique." },
  { name: "No news before 10 AM", category: "Mind", icon: "🚫", difficulty: "Medium", time: "Morning", keystone: false, science: "Morning cortisol is highest. Consuming crisis content spikes anxiety before your day begins." },

  // PERFORMANCE
  { name: "Plan top 3 priorities", category: "Performance", icon: "📋", difficulty: "Easy", time: "Morning", keystone: true, science: "The MIT (Most Important Tasks) method outperforms to-do lists by 3x in daily output studies." },
  { name: "Deep work session (90 min)", category: "Performance", icon: "⚡", difficulty: "Hard", time: "Morning", keystone: true, science: "Flow state takes 23 min to enter after distraction. 90-min blocks maximize peak output windows." },
  { name: "Review goals (weekly)", category: "Performance", icon: "🗺️", difficulty: "Easy", time: "Morning", keystone: true, science: "Written goals are 42% more likely to be achieved than unwritten ones (Dr. Gail Matthews, 2015)." },
  { name: "Inbox zero", category: "Performance", icon: "📬", difficulty: "Medium", time: "Anytime", keystone: false, science: "Context-switching to email costs 23 minutes of recovery time per interruption (UC Irvine study)." },
  { name: "Organize workspace", category: "Performance", icon: "🗂️", difficulty: "Easy", time: "Morning", keystone: false, science: "Cluttered environments raise cortisol. A clear desk creates a clear mental state." },
  { name: "Time-block your calendar", category: "Performance", icon: "🗓️", difficulty: "Medium", time: "Morning", keystone: false, science: "Parkinson's Law: work expands to fill time available. Blocks create urgency and compression." },
  { name: "No multitasking", category: "Performance", icon: "🎯", difficulty: "Hard", time: "Anytime", keystone: false, science: "Multitasking reduces IQ by 10–15 points — more than sleep deprivation (University of London)." },
  { name: "Single-tasking sprints (25 min)", category: "Performance", icon: "⏱️", difficulty: "Medium", time: "Anytime", keystone: false, science: "Pomodoro technique increases output by 25% and reduces mental fatigue through structured breaks." },

  // GROWTH
  { name: "Read 20 pages", category: "Growth", icon: "📚", difficulty: "Medium", time: "Evening", keystone: true, science: "20 pages/day = 18 books/year. Warren Buffett reads 500 pages daily. Knowledge compounds." },
  { name: "Learn one new thing", category: "Growth", icon: "🔬", difficulty: "Easy", time: "Anytime", keystone: false, science: "Neuroplasticity peaks with novel stimuli. Daily learning measurably slows cognitive aging." },
  { name: "Practice a skill (30 min)", category: "Growth", icon: "🎸", difficulty: "Medium", time: "Anytime", keystone: false, science: "10,000-hour rule requires deliberate practice — focused, feedback-rich repetition, not casual exposure." },
  { name: "Listen to educational podcast", category: "Growth", icon: "🎧", difficulty: "Easy", time: "Anytime", keystone: false, science: "Converts dead time (commuting, chores) into learning. Compounding knowledge with zero extra time." },
  { name: "Write 500 words", category: "Growth", icon: "✍️", difficulty: "Medium", time: "Anytime", keystone: false, science: "Writing externalizes thought and forces clarity. 'If you can't write it clearly, you don't think it clearly.'" },
  { name: "Review flashcards (Anki)", category: "Growth", icon: "🃏", difficulty: "Easy", time: "Anytime", keystone: false, science: "Spaced repetition is the most effective learning method known. Retention improves 200–400%." },

  // FINANCE
  { name: "Track daily expenses", category: "Finance", icon: "💳", difficulty: "Easy", time: "Evening", keystone: true, science: "Awareness of spending reduces it by 15–20%. What gets measured gets managed." },
  { name: "No-spend day", category: "Finance", icon: "🔒", difficulty: "Medium", time: "Anytime", keystone: false, science: "Builds financial discipline and surfaces unconscious spending patterns." },
  { name: "Invest / save fixed amount", category: "Finance", icon: "📈", difficulty: "Hard", time: "Anytime", keystone: true, science: "$100/month invested at 10% return = $1M in 45 years. Consistency, not amount, is the multiplier." },
  { name: "Review weekly budget", category: "Finance", icon: "📊", difficulty: "Medium", time: "Anytime", keystone: false, science: "People who review finances weekly accumulate 4x more wealth than those who check monthly." },
  { name: "One financial learning", category: "Finance", icon: "💡", difficulty: "Easy", time: "Anytime", keystone: false, science: "Financial literacy is the highest-ROI education. Most wealth destruction comes from ignorance, not bad luck." },

  // RELATIONSHIPS
  { name: "Call family / close friend", category: "Relationships", icon: "📞", difficulty: "Easy", time: "Anytime", keystone: true, science: "Harvard 85-year study found relationships — not wealth or fame — are the single predictor of happiness." },
  { name: "Express genuine gratitude", category: "Relationships", icon: "💌", difficulty: "Easy", time: "Anytime", keystone: false, science: "Expressing gratitude strengthens social bonds and boosts serotonin in both giver and receiver." },
  { name: "Help or mentor someone", category: "Relationships", icon: "🤝", difficulty: "Medium", time: "Anytime", keystone: false, science: "Givers rise to the top in long-term career studies (Adam Grant, Give and Take). Generosity compounds." },
  { name: "Network intentionally", category: "Relationships", icon: "🌐", difficulty: "Hard", time: "Anytime", keystone: false, science: "85% of jobs are filled through networking. One strategic relationship can change your entire trajectory." },
  { name: "Quality time with loved ones", category: "Relationships", icon: "❤️", difficulty: "Medium", time: "Evening", keystone: false, science: "Presence, not duration, defines relationship quality. Undivided attention is the rarest gift." },

  // DISCIPLINE
  { name: "Make your bed", category: "Discipline", icon: "🛏️", difficulty: "Easy", time: "Morning", keystone: true, science: "Admiral McRaven: completing one task first builds momentum. Keystone habit that triggers other order." },
  { name: "Cold plunge / ice bath", category: "Discipline", icon: "🧊", difficulty: "Hard", time: "Morning", keystone: false, science: "Voluntary discomfort builds stress tolerance. Proven by Andrew Huberman to boost norepinephrine 300%." },
  { name: "No phone first hour", category: "Discipline", icon: "📴", difficulty: "Hard", time: "Morning", keystone: true, science: "Checking your phone first thing hands your attention and agenda to others. Own your morning." },
  { name: "Weekly review (Sunday)", category: "Discipline", icon: "🔄", difficulty: "Medium", time: "Anytime", keystone: true, science: "GTD methodology. Weekly reviews create the clarity to operate in flow the entire following week." },
  { name: "Evening shutdown ritual", category: "Discipline", icon: "🌙", difficulty: "Easy", time: "Evening", keystone: false, science: "Defined work end-times reduce rumination and support psychological detachment — key to recovery." },
  { name: "Limit social media (< 30 min)", category: "Discipline", icon: "⏳", difficulty: "Hard", time: "Anytime", keystone: false, science: "Every hour of social media is associated with 8% lower life satisfaction (Hunt et al., 2018)." },
];

const CATEGORIES = ["Body", "Mind", "Performance", "Growth", "Finance", "Relationships", "Discipline"];

const CAT_CONFIG = {
  Body:          { color: "#ef6c4a", bg: "#1a0e0b", accent: "#ff8c69", glyph: "⚡" },
  Mind:          { color: "#7c6bef", bg: "#100e1a", accent: "#a89df5", glyph: "🧠" },
  Performance:   { color: "#f5c518", bg: "#1a1700", accent: "#ffd84d", glyph: "🎯" },
  Growth:        { color: "#4ade80", bg: "#081a0e", accent: "#86efac", glyph: "🚀" },
  Finance:       { color: "#38bdf8", bg: "#05121a", accent: "#7dd3fc", glyph: "💎" },
  Relationships: { color: "#f472b6", bg: "#1a0910", accent: "#f9a8d4", glyph: "❤️" },
  Discipline:    { color: "#fb923c", bg: "#1a0d05", accent: "#fdba74", glyph: "🔥" },
};

const DIFF_CONFIG = {
  Easy:   { color: "#4ade80", label: "EASY" },
  Medium: { color: "#f5c518", label: "MED" },
  Hard:   { color: "#ef6c4a", label: "HARD" },
};

const VIEWS = [
  { id: "dashboard", label: "Dashboard", icon: "◈" },
  { id: "daily",     label: "Daily",     icon: "◉" },
  { id: "weekly",    label: "Weekly",    icon: "◫" },
  { id: "analytics", label: "Analytics", icon: "◬" },
  { id: "library",   label: "Library",   icon: "◧" },
  { id: "reflect",   label: "Reflect",   icon: "◎" },
];

const today = () => new Date().toISOString().split("T")[0];
const dateLabel = (d) => new Date(d + "T00:00:00").toLocaleDateString("en-US", { weekday: "short", month: "short", day: "numeric" });
const weekNum = (d) => { const dt = new Date(d); const s = new Date(dt.getFullYear(), 0, 1); return Math.ceil(((dt - s) / 86400000 + s.getDay() + 1) / 7); };

// Seed: pick 8 elite keystone habits
const DEFAULT_HABITS = ELITE_HABITS.filter(h => h.keystone).slice(0, 8).map((h, i) => ({ ...h, id: i + 1 }));

// Seed logs: past 14 days with realistic completion
const seedLogs = () => {
  const logs = []; let id = 1000;
  DEFAULT_HABITS.forEach(h => {
    for (let i = 13; i >= 0; i--) {
      const d = new Date(); d.setDate(d.getDate() - i);
      const ds = d.toISOString().split("T")[0];
      const prob = h.difficulty === "Easy" ? 0.85 : h.difficulty === "Medium" ? 0.72 : 0.58;
      if (Math.random() < prob) logs.push({ id: id++, habitId: h.id, date: ds, completed: true, note: "" });
    }
  });
  return logs;
};

export default function App() {
  const [habits, setHabits] = useState(DEFAULT_HABITS);
  const [logs, setLogs] = useState(seedLogs);
  const [view, setView] = useState("dashboard");
  const [selDate, setSelDate] = useState(today());
  const [nextId, setNextId] = useState(500);
  const [showAdd, setShowAdd] = useState(false);
  const [libFilter, setLibFilter] = useState("All");
  const [expandedLib, setExpandedLib] = useState(null);
  const [reflections, setReflections] = useState([]);
  const [newRef, setNewRef] = useState({ wins: "", struggles: "", adjust: "", mood: 7, word: "" });
  const [newHabit, setNewHabit] = useState({ name: "", category: "Body", difficulty: "Easy", time: "Morning", icon: "⭐" });
  const [toast, setToast] = useState(null);
  const [animKey, setAnimKey] = useState(0);

  const showToast = (msg, type = "success") => {
    setToast({ msg, type });
    setTimeout(() => setToast(null), 2500);
  };

  const getLog = useCallback((hid, date) => logs.find(l => l.habitId === hid && l.date === date), [logs]);

  const toggle = useCallback((hid, date) => {
    const ex = getLog(hid, date);
    if (ex) setLogs(ls => ls.map(l => l.id === ex.id ? { ...l, completed: !l.completed } : l));
    else setLogs(ls => [...ls, { id: nextId, habitId: hid, date, completed: true, note: "" }]);
    setNextId(n => n + 1);
  }, [getLog, nextId]);

  const getStreak = useCallback((hid) => {
    let streak = 0, d = new Date();
    while (true) {
      const ds = d.toISOString().split("T")[0];
      const l = logs.find(x => x.habitId === hid && x.date === ds && x.completed);
      if (l) { streak++; d.setDate(d.getDate() - 1); } else break;
    }
    return streak;
  }, [logs]);

  const completionRate = useCallback((hid, days = 7) => {
    let c = 0;
    for (let i = 0; i < days; i++) {
      const d = new Date(); d.setDate(d.getDate() - i);
      const ds = d.toISOString().split("T")[0];
      if (logs.find(l => l.habitId === hid && l.date === ds && l.completed)) c++;
    }
    return Math.round((c / days) * 100);
  }, [logs]);

  const addHabit = () => {
    if (!newHabit.name.trim()) return;
    setHabits(h => [...h, { ...newHabit, id: nextId, keystone: false, science: "" }]);
    setNextId(n => n + 1);
    setShowAdd(false);
    showToast(`"${newHabit.name}" added to your stack 🔥`);
  };

  const addFromLib = (lib) => {
    if (habits.find(h => h.name === lib.name)) { showToast("Already in your stack!", "warn"); return; }
    setHabits(h => [...h, { ...lib, id: nextId, keystone: lib.keystone || false }]);
    setNextId(n => n + 1);
    showToast(`"${lib.name}" added! 🚀`);
  };

  const removeHabit = (id) => {
    setHabits(h => h.filter(x => x.id !== id));
    setLogs(l => l.filter(x => x.habitId !== id));
  };

  // Dashboard stats
  const todayHabits = habits;
  const todayDone = todayHabits.filter(h => getLog(h.id, today())?.completed).length;
  const todayPct = todayHabits.length ? Math.round((todayDone / todayHabits.length) * 100) : 0;
  const totalStreaks = habits.reduce((a, h) => a + getStreak(h.id), 0);
  const longestStreak = habits.reduce((a, h) => Math.max(a, getStreak(h.id)), 0);
  const weeklyAvg = habits.length ? Math.round(habits.reduce((a, h) => a + completionRate(h.id, 7), 0) / habits.length) : 0;

  // Last 7 days for spark lines
  const last7 = Array.from({ length: 7 }, (_, i) => {
    const d = new Date(); d.setDate(d.getDate() - (6 - i));
    return d.toISOString().split("T")[0];
  });

  const weekDates = useMemo(() => {
    const arr = [], base = new Date(selDate + "T00:00:00");
    const day = base.getDay(), monday = new Date(base);
    monday.setDate(base.getDate() - (day === 0 ? 6 : day - 1));
    for (let i = 0; i < 7; i++) { const d = new Date(monday); d.setDate(monday.getDate() + i); arr.push(d.toISOString().split("T")[0]); }
    return arr;
  }, [selDate]);

  const css = `
    @import url('https://fonts.googleapis.com/css2?family=Syne:wght@400;500;600;700;800&family=DM+Mono:wght@300;400;500&display=swap');
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body { background: #080808; }
    ::-webkit-scrollbar { width: 4px; } ::-webkit-scrollbar-track { background: #111; } ::-webkit-scrollbar-thumb { background: #333; border-radius: 2px; }
    .habit-card:hover { transform: translateY(-1px); box-shadow: 0 8px 32px rgba(0,0,0,0.5) !important; }
    .nav-btn:hover { background: #1a1a1a !important; }
    .lib-card:hover { border-color: #333 !important; background: #141414 !important; }
    .check-ring:hover { opacity: 0.85; transform: scale(1.08); }
    .pill:hover { opacity: 0.8; }
    @keyframes slideUp { from { opacity:0; transform:translateY(16px); } to { opacity:1; transform:none; } }
    @keyframes pulse { 0%,100% { opacity:1; } 50% { opacity:0.5; } }
    @keyframes shimmer { 0% { background-position:-200% 0; } 100% { background-position:200% 0; } }
    @keyframes toastIn { from { opacity:0; transform:translateX(20px); } to { opacity:1; transform:none; } }
    .anim-up { animation: slideUp 0.4s ease both; }
    .toast { animation: toastIn 0.3s ease both; }

    /* ── Responsive layout ── */
    .sidebar { display: flex; }
    .mobile-topbar { display: none; }
    .mobile-bottomnav { display: none; }
    .main-content { margin-left: 220px; padding: 32px 36px; min-height: 100vh; }

    @media (max-width: 768px) {
      .sidebar { display: none !important; }
      .mobile-topbar { display: flex !important; }
      .mobile-bottomnav { display: flex !important; }
      .main-content { margin-left: 0 !important; padding: 72px 14px 90px !important; }
    }
  `;

  const S = {
    app: { fontFamily: "'Syne', sans-serif", background: "#080808", minHeight: "100vh", color: "#e8e8e8" },
    sidebar: { width: "220px", background: "#0c0c0c", borderRight: "1px solid #1a1a1a", flexDirection: "column", padding: "24px 0", position: "fixed", top: 0, left: 0, bottom: 0, zIndex: 100 },
    logo: { padding: "0 20px 28px", borderBottom: "1px solid #1a1a1a" },
    logoText: { fontSize: "18px", fontWeight: "800", color: "#fff", letterSpacing: "-0.5px" },
    logoSub: { fontSize: "10px", color: "#444", fontFamily: "'DM Mono', monospace", letterSpacing: "2px", marginTop: "3px" },
    nav: { padding: "20px 12px", flex: 1 },
    navBtn: (active) => ({ display: "flex", alignItems: "center", gap: "10px", padding: "10px 12px", borderRadius: "8px", border: "none", background: active ? "#1a1a1a" : "transparent", color: active ? "#fff" : "#555", fontWeight: active ? "700" : "500", fontSize: "14px", cursor: "pointer", width: "100%", textAlign: "left", fontFamily: "'Syne', sans-serif", marginBottom: "3px", transition: "all 0.15s" }),
    navIcon: (active) => ({ fontSize: "16px", color: active ? "#f5c518" : "#444", fontWeight: "700" }),
    main: { minHeight: "100vh" },
    topBar: { display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "24px", flexWrap: "wrap", gap: "10px" },
    pageTitle: { fontSize: "28px", fontWeight: "800", color: "#fff", letterSpacing: "-0.5px" },
    pageSub: { fontSize: "13px", color: "#555", fontFamily: "'DM Mono', monospace", marginTop: "4px" },
    btn: (v = "gold") => ({
      padding: "10px 20px", borderRadius: "8px", border: "none", cursor: "pointer", fontSize: "13px", fontWeight: "700", fontFamily: "'Syne', sans-serif", transition: "all 0.15s",
      background: v === "gold" ? "#f5c518" : v === "outline" ? "transparent" : v === "danger" ? "#2a0a0a" : "#1a1a1a",
      color: v === "gold" ? "#080808" : v === "outline" ? "#555" : v === "danger" ? "#ef6c4a" : "#888",
      border: v === "outline" ? "1px solid #2a2a2a" : v === "danger" ? "1px solid #3a1a1a" : "none",
    }),
    card: (glow = null) => ({ background: "#111", borderRadius: "14px", border: `1px solid ${glow ? glow + "22" : "#1e1e1e"}`, padding: "22px", boxShadow: glow ? `0 0 30px ${glow}11` : "none", transition: "all 0.2s" }),
    statCard: (color) => ({ background: "#111", borderRadius: "14px", border: `1px solid ${color}22`, padding: "20px 22px", flex: 1 }),
    mono: { fontFamily: "'DM Mono', monospace" },
    tag: (color) => ({ padding: "3px 10px", borderRadius: "20px", fontSize: "10px", fontWeight: "700", background: color + "22", color: color, letterSpacing: "1px", fontFamily: "'DM Mono', monospace", display: "inline-flex", alignItems: "center" }),
    ring: (done, color) => ({ width: "32px", height: "32px", borderRadius: "50%", border: done ? "none" : `2px solid #333`, background: done ? color : "transparent", display: "flex", alignItems: "center", justifyContent: "center", cursor: "pointer", flexShrink: 0, transition: "all 0.2s", fontSize: "13px" }),
    bar: (pct, color) => ({ height: "4px", background: "#1e1e1e", borderRadius: "2px", overflow: "hidden", position: "relative", flex: 1, "::after": {} }),
    barFill: (pct, color) => ({ height: "100%", width: pct + "%", background: color, borderRadius: "2px", transition: "width 0.8s cubic-bezier(0.16,1,0.3,1)" }),
    modal: { position: "fixed", inset: 0, background: "rgba(0,0,0,0.85)", display: "flex", alignItems: "center", justifyContent: "center", zIndex: 999, backdropFilter: "blur(8px)" },
    modalBox: { background: "#111", border: "1px solid #2a2a2a", borderRadius: "20px", padding: "36px", width: "520px", maxWidth: "95vw", maxHeight: "90vh", overflowY: "auto" },
    input: { width: "100%", padding: "11px 14px", border: "1px solid #2a2a2a", borderRadius: "8px", fontSize: "14px", fontFamily: "'Syne', sans-serif", background: "#0c0c0c", color: "#e8e8e8", outline: "none" },
    select: { padding: "11px 14px", border: "1px solid #2a2a2a", borderRadius: "8px", fontSize: "13px", fontFamily: "'Syne', sans-serif", background: "#0c0c0c", color: "#e8e8e8", cursor: "pointer", outline: "none" },
    label: { fontSize: "11px", fontWeight: "700", color: "#555", marginBottom: "6px", display: "block", textTransform: "uppercase", letterSpacing: "1.5px", fontFamily: "'DM Mono', monospace" },
    divider: { height: "1px", background: "#1a1a1a", margin: "24px 0" },
  };

  const ProgressRing = ({ pct, size = 80, stroke = 6, color = "#f5c518", children }) => {
    const r = (size - stroke) / 2, circ = 2 * Math.PI * r, dash = (pct / 100) * circ;
    return (
      <div style={{ position: "relative", width: size, height: size, flexShrink: 0 }}>
        <svg width={size} height={size} style={{ transform: "rotate(-90deg)" }}>
          <circle cx={size/2} cy={size/2} r={r} fill="none" stroke="#1e1e1e" strokeWidth={stroke} />
          <circle cx={size/2} cy={size/2} r={r} fill="none" stroke={color} strokeWidth={stroke} strokeDasharray={`${dash} ${circ}`} strokeLinecap="round" style={{ transition: "stroke-dasharray 1s ease" }} />
        </svg>
        <div style={{ position: "absolute", inset: 0, display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center" }}>{children}</div>
      </div>
    );
  };

  const SparkBar = ({ hid, color }) => (
    <div style={{ display: "flex", gap: "2px", alignItems: "flex-end", height: "24px" }}>
      {last7.map(d => {
        const done = logs.find(l => l.habitId === hid && l.date === d && l.completed);
        return <div key={d} style={{ width: "6px", height: done ? "20px" : "6px", background: done ? color : "#1e1e1e", borderRadius: "2px", transition: "height 0.4s ease" }} />;
      })}
    </div>
  );

  const HabitRow = ({ h, date }) => {
    const log = getLog(h.id, date);
    const done = log?.completed || false;
    const streak = getStreak(h.id);
    const cfg = CAT_CONFIG[h.category] || CAT_CONFIG.Body;
    return (
      <div className="habit-card" style={{ display: "flex", alignItems: "center", gap: "14px", padding: "16px 18px", background: done ? cfg.bg : "#111", borderRadius: "12px", marginBottom: "8px", border: `1px solid ${done ? cfg.color + "33" : "#1e1e1e"}`, transition: "all 0.2s" }}>
        <div className="check-ring" style={S.ring(done, cfg.color)} onClick={() => toggle(h.id, date)} title="Toggle">
          {done && <span style={{ color: "#000", fontWeight: "900", fontSize: "12px" }}>✓</span>}
        </div>
        <span style={{ fontSize: "18px" }}>{h.icon}</span>
        <div style={{ flex: 1, minWidth: 0 }}>
          <div style={{ display: "flex", alignItems: "center", gap: "8px", flexWrap: "wrap" }}>
            <span style={{ fontWeight: "700", fontSize: "14px", color: done ? cfg.color : "#e8e8e8", textDecoration: done ? "line-through" : "none" }}>{h.name}</span>
            {h.keystone && <span style={S.tag("#f5c518")}>KEYSTONE</span>}
          </div>
          <div style={{ display: "flex", gap: "6px", marginTop: "5px", flexWrap: "wrap", alignItems: "center" }}>
            <span style={S.tag(cfg.color)}>{h.category.toUpperCase()}</span>
            <span style={S.tag(DIFF_CONFIG[h.difficulty].color)}>{DIFF_CONFIG[h.difficulty].label}</span>
            <span style={{ ...S.mono, fontSize: "11px", color: "#444" }}>{h.time}</span>
          </div>
        </div>
        <SparkBar hid={h.id} color={cfg.color} />
        {streak > 0 && (
          <div style={{ textAlign: "right", flexShrink: 0 }}>
            <div style={{ fontSize: "16px", fontWeight: "800", color: "#f5c518" }}>🔥{streak}</div>
            <div style={{ ...S.mono, fontSize: "10px", color: "#444" }}>streak</div>
          </div>
        )}
        <button onClick={() => removeHabit(h.id)} style={{ background: "none", border: "none", cursor: "pointer", color: "#333", fontSize: "12px", padding: "4px" }}>✕</button>
      </div>
    );
  };

  return (
    <div style={S.app}>
      <style>{css}</style>

      {/* Toast */}
      {toast && (
        <div className="toast" style={{ position: "fixed", top: "20px", right: "20px", zIndex: 9999, background: toast.type === "warn" ? "#2a1a00" : "#0a1a0a", border: `1px solid ${toast.type === "warn" ? "#f5c51833" : "#4ade8033"}`, color: toast.type === "warn" ? "#f5c518" : "#4ade80", padding: "12px 18px", borderRadius: "10px", fontSize: "13px", fontWeight: "700", maxWidth: "300px" }}>
          {toast.msg}
        </div>
      )}

      {/* Mobile Top Bar */}
      <div className="mobile-topbar" style={{ position: "fixed", top: 0, left: 0, right: 0, zIndex: 100, background: "#0c0c0c", borderBottom: "1px solid #1a1a1a", padding: "12px 16px", alignItems: "center", justifyContent: "space-between" }}>
        <div>
          <div style={{ fontSize: "16px", fontWeight: "800", color: "#fff", letterSpacing: "-0.5px" }}>APEX HABITS</div>
          <div style={{ fontSize: "9px", color: "#444", fontFamily: "'DM Mono', monospace", letterSpacing: "2px" }}>TOP 1% SYSTEM</div>
        </div>
        <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
          <div style={{ ...S.mono, fontSize: "12px", color: "#f5c518", fontWeight: "700" }}>{todayPct}%</div>
          <button style={{ ...S.btn("gold"), padding: "8px 14px", fontSize: "12px" }} onClick={() => setShowAdd(true)}>+ Add</button>
        </div>
      </div>

      {/* Desktop Sidebar */}
      <div className="sidebar" style={S.sidebar}>
        <div style={S.logo}>
          <div style={S.logoText}>APEX HABITS</div>
          <div style={S.logoSub}>TOP 1% SYSTEM</div>
        </div>
        <div style={S.nav}>
          {VIEWS.map(v => (
            <button key={v.id} className="nav-btn" style={S.navBtn(view === v.id)} onClick={() => setView(v.id)}>
              <span style={S.navIcon(view === v.id)}>{v.icon}</span>
              {v.label}
            </button>
          ))}
        </div>
        {/* Stats in sidebar */}
        <div style={{ padding: "16px 20px", borderTop: "1px solid #1a1a1a" }}>
          <div style={{ ...S.mono, fontSize: "10px", color: "#444", marginBottom: "12px", letterSpacing: "1px" }}>TODAY</div>
          <div style={{ display: "flex", alignItems: "center", gap: "10px", marginBottom: "8px" }}>
            <div style={{ flex: 1, height: "4px", background: "#1e1e1e", borderRadius: "2px" }}>
              <div style={{ height: "100%", width: todayPct + "%", background: todayPct >= 80 ? "#4ade80" : todayPct >= 50 ? "#f5c518" : "#ef6c4a", borderRadius: "2px", transition: "width 0.8s ease" }} />
            </div>
            <span style={{ ...S.mono, fontSize: "12px", color: "#fff", fontWeight: "700" }}>{todayPct}%</span>
          </div>
          <div style={{ ...S.mono, fontSize: "11px", color: "#555" }}>{todayDone}/{habits.length} habits done</div>
        </div>
        <div style={{ padding: "12px 20px" }}>
          <button style={{ ...S.btn("gold"), width: "100%", textAlign: "center" }} onClick={() => setShowAdd(true)}>+ Add Habit</button>
        </div>
      </div>

      {/* Mobile Bottom Nav */}
      <div className="mobile-bottomnav" style={{ position: "fixed", bottom: 0, left: 0, right: 0, zIndex: 100, background: "#0c0c0c", borderTop: "1px solid #1a1a1a", padding: "8px 4px", justifyContent: "space-around", alignItems: "center" }}>
        {VIEWS.map(v => {
          const active = view === v.id;
          return (
            <button key={v.id} onClick={() => setView(v.id)} style={{ display: "flex", flexDirection: "column", alignItems: "center", gap: "3px", background: "none", border: "none", cursor: "pointer", padding: "6px 8px", borderRadius: "8px", flex: 1 }}>
              <span style={{ fontSize: "18px", opacity: active ? 1 : 0.35 }}>{v.icon}</span>
              <span style={{ fontSize: "9px", fontFamily: "'Syne', sans-serif", fontWeight: "700", color: active ? "#f5c518" : "#555", letterSpacing: "0.5px" }}>{v.label}</span>
            </button>
          );
        })}
      </div>

      {/* Main Content */}
      <div className="main-content" style={S.main}>

        {/* DASHBOARD */}
        {view === "dashboard" && (
          <div className="anim-up">
            <div style={S.topBar}>
              <div>
                <div style={S.pageTitle}>Command Center</div>
                <div style={S.pageSub}>{dateLabel(today())} · Week {weekNum(today())}</div>
              </div>
            </div>

            {/* Top stat row */}
            <div style={{ display: "flex", gap: "14px", marginBottom: "24px", flexWrap: "wrap" }}>
              {[
                { label: "Today's Score", value: todayPct + "%", color: "#f5c518", sub: `${todayDone} of ${habits.length} complete` },
                { label: "Weekly Avg", value: weeklyAvg + "%", color: "#4ade80", sub: "last 7 days" },
                { label: "Total Streaks", value: totalStreaks, color: "#7c6bef", sub: "combined days" },
                { label: "Best Streak", value: longestStreak + "d", color: "#ef6c4a", sub: "current longest" },
              ].map((s, i) => (
                <div key={i} style={{ ...S.statCard(s.color), minWidth: "160px" }}>
                  <div style={{ ...S.mono, fontSize: "10px", color: "#555", letterSpacing: "1px", marginBottom: "8px" }}>{s.label.toUpperCase()}</div>
                  <div style={{ fontSize: "32px", fontWeight: "800", color: s.color, lineHeight: 1 }}>{s.value}</div>
                  <div style={{ ...S.mono, fontSize: "11px", color: "#444", marginTop: "6px" }}>{s.sub}</div>
                </div>
              ))}
            </div>

            {/* Today's progress ring + habit list */}
            <div style={{ display: "flex", gap: "20px", flexWrap: "wrap" }}>
              <div style={{ ...S.card(), flex: "0 0 200px", display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", gap: "16px" }}>
                <ProgressRing pct={todayPct} size={130} stroke={8} color={todayPct >= 80 ? "#4ade80" : todayPct >= 50 ? "#f5c518" : "#ef6c4a"}>
                  <div style={{ fontSize: "24px", fontWeight: "800", color: "#fff" }}>{todayPct}%</div>
                  <div style={{ ...S.mono, fontSize: "9px", color: "#555", letterSpacing: "1px" }}>TODAY</div>
                </ProgressRing>
                <div style={{ textAlign: "center" }}>
                  <div style={{ fontSize: "13px", fontWeight: "700", color: todayPct === 100 ? "#4ade80" : "#888" }}>
                    {todayPct === 100 ? "🏆 Perfect Day!" : todayPct >= 80 ? "🔥 On fire!" : todayPct >= 50 ? "💪 Keep going!" : "⚡ Start now"}
                  </div>
                </div>
              </div>

              <div style={{ flex: 1, minWidth: "280px" }}>
                <div style={{ ...S.mono, fontSize: "10px", color: "#444", letterSpacing: "1px", marginBottom: "14px" }}>KEYSTONE HABITS</div>
                {habits.filter(h => h.keystone).map(h => <HabitRow key={h.id} h={h} date={today()} />)}
                {habits.filter(h => !h.keystone).length > 0 && <>
                  <div style={{ ...S.mono, fontSize: "10px", color: "#444", letterSpacing: "1px", margin: "18px 0 12px" }}>OTHER HABITS</div>
                  {habits.filter(h => !h.keystone).map(h => <HabitRow key={h.id} h={h} date={today()} />)}
                </>}
                {habits.length === 0 && <div style={{ textAlign: "center", padding: "40px", color: "#444", fontSize: "14px" }}>No habits yet. Add your first keystone habit →</div>}
              </div>
            </div>

            {/* Category breakdown */}
            <div style={{ ...S.divider }} />
            <div style={{ ...S.mono, fontSize: "10px", color: "#444", letterSpacing: "1px", marginBottom: "14px" }}>PERFORMANCE BY CATEGORY</div>
            <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(200px, 1fr))", gap: "10px" }}>
              {CATEGORIES.map(cat => {
                const catHabits = habits.filter(h => h.category === cat);
                if (!catHabits.length) return null;
                const cfg = CAT_CONFIG[cat];
                const rate = Math.round(catHabits.reduce((a, h) => a + completionRate(h.id, 7), 0) / catHabits.length);
                return (
                  <div key={cat} style={{ background: "#111", border: `1px solid ${cfg.color}22`, borderRadius: "10px", padding: "14px 16px" }}>
                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "8px" }}>
                      <span style={{ fontSize: "13px", fontWeight: "700", color: cfg.color }}>{cfg.glyph} {cat}</span>
                      <span style={{ ...S.mono, fontSize: "12px", color: "#fff", fontWeight: "700" }}>{rate}%</span>
                    </div>
                    <div style={S.bar(rate, cfg.color)}>
                      <div style={S.barFill(rate, cfg.color)} />
                    </div>
                    <div style={{ ...S.mono, fontSize: "10px", color: "#444", marginTop: "6px" }}>{catHabits.length} habit{catHabits.length > 1 ? "s" : ""} · 7-day avg</div>
                  </div>
                );
              })}
            </div>
          </div>
        )}

        {/* DAILY */}
        {view === "daily" && (
          <div className="anim-up">
            <div style={S.topBar}>
              <div>
                <div style={S.pageTitle}>Daily Stack</div>
                <div style={S.pageSub}>{dateLabel(selDate)}</div>
              </div>
              <input type="date" value={selDate} onChange={e => setSelDate(e.target.value)} style={{ ...S.input, width: "160px", fontSize: "13px" }} />
            </div>
            {["Morning", "Afternoon", "Evening", "Anytime"].map(time => {
              const hs = habits.filter(h => h.time === time);
              if (!hs.length) return null;
              const done = hs.filter(h => getLog(h.id, selDate)?.completed).length;
              return (
                <div key={time} style={{ marginBottom: "28px" }}>
                  <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", marginBottom: "12px" }}>
                    <div style={{ ...S.mono, fontSize: "11px", color: "#555", letterSpacing: "1.5px" }}>{time.toUpperCase()}</div>
                    <div style={{ ...S.mono, fontSize: "11px", color: "#444" }}>{done}/{hs.length}</div>
                  </div>
                  {hs.map(h => <HabitRow key={h.id} h={h} date={selDate} />)}
                </div>
              );
            })}
            {habits.length === 0 && <div style={{ textAlign: "center", padding: "60px", color: "#444" }}>No habits yet. Build your stack 🏗️</div>}
          </div>
        )}

        {/* WEEKLY */}
        {view === "weekly" && (
          <div className="anim-up">
            <div style={S.topBar}>
              <div>
                <div style={S.pageTitle}>Weekly View</div>
                <div style={S.pageSub}>Week {weekNum(weekDates[0])} · {dateLabel(weekDates[0])} → {dateLabel(weekDates[6])}</div>
              </div>
              <input type="date" value={selDate} onChange={e => setSelDate(e.target.value)} style={{ ...S.input, width: "160px", fontSize: "13px" }} />
            </div>
            <div style={{ overflowX: "auto" }}>
              <table style={{ width: "100%", borderCollapse: "separate", borderSpacing: "0 6px", fontSize: "13px" }}>
                <thead>
                  <tr>
                    <th style={{ textAlign: "left", padding: "8px 14px", color: "#555", fontWeight: "700", ...S.mono, fontSize: "10px", letterSpacing: "1px" }}>HABIT</th>
                    {weekDates.map(d => (
                      <th key={d} style={{ padding: "8px", textAlign: "center", color: d === today() ? "#f5c518" : "#444", ...S.mono, fontSize: "10px", minWidth: "52px" }}>
                        <div>{new Date(d + "T00:00:00").toLocaleDateString("en-US", { weekday: "short" }).toUpperCase()}</div>
                        <div style={{ fontSize: "12px", color: d === today() ? "#f5c518" : "#555" }}>{new Date(d + "T00:00:00").getDate()}</div>
                      </th>
                    ))}
                    <th style={{ padding: "8px", textAlign: "center", color: "#555", ...S.mono, fontSize: "10px" }}>7D</th>
                  </tr>
                </thead>
                <tbody>
                  {habits.map(h => {
                    const cfg = CAT_CONFIG[h.category] || CAT_CONFIG.Body;
                    const rate = completionRate(h.id, 7);
                    return (
                      <tr key={h.id} style={{ background: "#111", borderRadius: "10px" }}>
                        <td style={{ padding: "12px 14px", borderRadius: "10px 0 0 10px", borderLeft: `3px solid ${cfg.color}` }}>
                          <div style={{ fontWeight: "700", color: "#e8e8e8", fontSize: "13px" }}>{h.icon} {h.name}</div>
                          <span style={S.tag(cfg.color)}>{h.category.toUpperCase()}</span>
                        </td>
                        {weekDates.map(d => {
                          const done = getLog(h.id, d)?.completed;
                          return (
                            <td key={d} style={{ textAlign: "center", background: d === today() ? "#141414" : "transparent" }}>
                              <div style={{ ...S.ring(done, cfg.color), margin: "0 auto" }} onClick={() => toggle(h.id, d)}>
                                {done && <span style={{ color: "#000", fontWeight: "900", fontSize: "11px" }}>✓</span>}
                              </div>
                            </td>
                          );
                        })}
                        <td style={{ textAlign: "center", borderRadius: "0 10px 10px 0", padding: "0 10px" }}>
                          <span style={{ ...S.mono, fontWeight: "700", fontSize: "13px", color: rate >= 80 ? "#4ade80" : rate >= 50 ? "#f5c518" : "#ef6c4a" }}>{rate}%</span>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* ANALYTICS */}
        {view === "analytics" && (
          <div className="anim-up">
            <div style={S.topBar}><div><div style={S.pageTitle}>Analytics</div><div style={S.pageSub}>14-day performance intelligence</div></div></div>
            <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(320px, 1fr))", gap: "16px" }}>
              {[...habits].sort((a, b) => completionRate(b.id, 14) - completionRate(a.id, 14)).map(h => {
                const cfg = CAT_CONFIG[h.category] || CAT_CONFIG.Body;
                const rate7 = completionRate(h.id, 7);
                const rate14 = completionRate(h.id, 14);
                const streak = getStreak(h.id);
                const trend = rate7 - rate14;
                return (
                  <div key={h.id} style={S.card(cfg.color)}>
                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start", marginBottom: "16px" }}>
                      <div>
                        <div style={{ fontWeight: "700", fontSize: "15px", color: "#fff", marginBottom: "4px" }}>{h.icon} {h.name}</div>
                        <div style={{ display: "flex", gap: "6px" }}>
                          <span style={S.tag(cfg.color)}>{h.category.toUpperCase()}</span>
                          <span style={S.tag(DIFF_CONFIG[h.difficulty].color)}>{DIFF_CONFIG[h.difficulty].label}</span>
                        </div>
                      </div>
                      <ProgressRing pct={rate14} size={56} stroke={5} color={cfg.color}>
                        <span style={{ fontSize: "10px", fontWeight: "800", color: cfg.color }}>{rate14}%</span>
                      </ProgressRing>
                    </div>
                    <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr 1fr", gap: "8px", marginBottom: "14px" }}>
                      {[
                        { label: "7-Day", val: rate7 + "%", color: rate7 >= 70 ? "#4ade80" : "#f5c518" },
                        { label: "Streak", val: streak + "d", color: "#f5c518" },
                        { label: "Trend", val: (trend >= 0 ? "▲" : "▼") + Math.abs(trend) + "%", color: trend >= 0 ? "#4ade80" : "#ef6c4a" },
                      ].map(s => (
                        <div key={s.label} style={{ background: "#0c0c0c", borderRadius: "8px", padding: "10px", textAlign: "center" }}>
                          <div style={{ ...S.mono, fontSize: "9px", color: "#444", marginBottom: "4px" }}>{s.label.toUpperCase()}</div>
                          <div style={{ fontWeight: "800", fontSize: "16px", color: s.color }}>{s.val}</div>
                        </div>
                      ))}
                    </div>
                    <div style={{ ...S.mono, fontSize: "10px", color: "#444", marginBottom: "6px" }}>LAST 7 DAYS</div>
                    <div style={{ display: "flex", gap: "3px", alignItems: "flex-end", height: "32px" }}>
                      {last7.map(d => {
                        const done = logs.find(l => l.habitId === h.id && l.date === d && l.completed);
                        return <div key={d} style={{ flex: 1, height: done ? "28px" : "8px", background: done ? cfg.color : "#1e1e1e", borderRadius: "3px", transition: "height 0.4s ease" }} />;
                      })}
                    </div>
                    {h.science && <div style={{ marginTop: "12px", padding: "10px 12px", background: "#0c0c0c", borderRadius: "8px", fontSize: "11px", color: "#666", lineHeight: "1.5", fontStyle: "italic", borderLeft: `2px solid ${cfg.color}44` }}>{h.science}</div>}
                  </div>
                );
              })}
            </div>
            {habits.length === 0 && <div style={{ textAlign: "center", padding: "60px", color: "#444" }}>Add habits to see analytics 📊</div>}
          </div>
        )}

        {/* LIBRARY */}
        {view === "library" && (
          <div className="anim-up">
            <div style={S.topBar}>
              <div>
                <div style={S.pageTitle}>Habit Library</div>
                <div style={S.pageSub}>50 elite habits used by top 1% performers — science-backed</div>
              </div>
            </div>
            <div style={{ display: "flex", gap: "8px", marginBottom: "24px", flexWrap: "wrap" }}>
              {["All", ...CATEGORIES].map(cat => {
                const cfg = cat !== "All" ? CAT_CONFIG[cat] : null;
                const active = libFilter === cat;
                return (
                  <button key={cat} className="pill" onClick={() => setLibFilter(cat)} style={{ padding: "7px 14px", borderRadius: "20px", border: `1px solid ${active ? (cfg?.color || "#f5c518") : "#2a2a2a"}`, background: active ? (cfg?.color || "#f5c518") + "22" : "transparent", color: active ? (cfg?.color || "#f5c518") : "#555", fontSize: "12px", fontWeight: "700", cursor: "pointer", fontFamily: "'Syne', sans-serif", transition: "all 0.15s" }}>
                    {cat}
                  </button>
                );
              })}
            </div>
            <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))", gap: "12px" }}>
              {ELITE_HABITS.filter(h => libFilter === "All" || h.category === libFilter).map((lib, i) => {
                const cfg = CAT_CONFIG[lib.category];
                const added = habits.find(h => h.name === lib.name);
                const expanded = expandedLib === i;
                return (
                  <div key={i} className="lib-card" style={{ background: "#111", border: `1px solid ${added ? cfg.color + "44" : "#1e1e1e"}`, borderRadius: "12px", padding: "16px", transition: "all 0.2s", cursor: "pointer" }} onClick={() => setExpandedLib(expanded ? null : i)}>
                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
                      <div style={{ flex: 1, marginRight: "10px" }}>
                        <div style={{ fontWeight: "700", fontSize: "14px", color: "#e8e8e8", marginBottom: "6px" }}>{lib.icon} {lib.name}</div>
                        <div style={{ display: "flex", gap: "5px", flexWrap: "wrap" }}>
                          <span style={S.tag(cfg.color)}>{lib.category.toUpperCase()}</span>
                          <span style={S.tag(DIFF_CONFIG[lib.difficulty].color)}>{DIFF_CONFIG[lib.difficulty].label}</span>
                          {lib.keystone && <span style={S.tag("#f5c518")}>KEYSTONE</span>}
                        </div>
                      </div>
                      <button onClick={e => { e.stopPropagation(); addFromLib(lib); }} style={{ ...S.btn(added ? "outline" : "gold"), padding: "6px 12px", fontSize: "12px", flexShrink: 0 }}>
                        {added ? "✓ Added" : "+ Add"}
                      </button>
                    </div>
                    {expanded && lib.science && (
                      <div style={{ marginTop: "12px", padding: "10px 12px", background: "#0c0c0c", borderRadius: "8px", fontSize: "12px", color: "#888", lineHeight: "1.6", borderLeft: `2px solid ${cfg.color}` }}>
                        🔬 {lib.science}
                      </div>
                    )}
                    {!expanded && <div style={{ ...S.mono, fontSize: "10px", color: "#333", marginTop: "8px" }}>Click to see science ↓</div>}
                  </div>
                );
              })}
            </div>
          </div>
        )}

        {/* REFLECT */}
        {view === "reflect" && (
          <div className="anim-up">
            <div style={S.topBar}><div><div style={S.pageTitle}>Weekly Reflection</div><div style={S.pageSub}>Week {weekNum(today())} · High performers review weekly</div></div></div>
            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "20px", flexWrap: "wrap" }}>
              <div style={{ ...S.card(), gridColumn: "1 / -1" }}>
                <div style={{ ...S.mono, fontSize: "10px", color: "#555", letterSpacing: "1px", marginBottom: "20px" }}>WEEK {weekNum(today())} PERFORMANCE SNAPSHOT</div>
                <div style={{ display: "flex", gap: "16px", flexWrap: "wrap" }}>
                  <ProgressRing pct={weeklyAvg} size={90} stroke={7} color={weeklyAvg >= 80 ? "#4ade80" : "#f5c518"}>
                    <span style={{ fontSize: "16px", fontWeight: "800", color: "#fff" }}>{weeklyAvg}%</span>
                  </ProgressRing>
                  <div style={{ flex: 1 }}>
                    {[...habits].sort((a, b) => completionRate(b.id, 7) - completionRate(a.id, 7)).slice(0, 5).map(h => {
                      const rate = completionRate(h.id, 7);
                      const cfg = CAT_CONFIG[h.category];
                      return (
                        <div key={h.id} style={{ display: "flex", alignItems: "center", gap: "10px", marginBottom: "8px" }}>
                          <span style={{ ...S.mono, fontSize: "12px", color: "#888", width: "120px", overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>{h.icon} {h.name}</span>
                          <div style={{ flex: 1, height: "4px", background: "#1e1e1e", borderRadius: "2px" }}>
                            <div style={{ height: "100%", width: rate + "%", background: cfg.color, borderRadius: "2px", transition: "width 0.8s ease" }} />
                          </div>
                          <span style={{ ...S.mono, fontSize: "11px", color: rate >= 70 ? "#4ade80" : "#f5c518", width: "35px", textAlign: "right" }}>{rate}%</span>
                        </div>
                      );
                    })}
                  </div>
                </div>
              </div>
              {[
                { key: "wins", label: "🏆 Wins This Week", placeholder: "What went well? What momentum did you build?" },
                { key: "struggles", label: "⚡ What I Struggled With", placeholder: "Which habits did you skip? What obstacles showed up?" },
                { key: "adjust", label: "🔧 Adjustments for Next Week", placeholder: "What will you change? What will you protect?" },
              ].map(field => (
                <div key={field.key} style={S.card()}>
                  <label style={S.label}>{field.label}</label>
                  <textarea style={{ ...S.input, height: "100px", resize: "vertical", lineHeight: "1.6" }} placeholder={field.placeholder} value={newRef[field.key]} onChange={e => setNewRef({ ...newRef, [field.key]: e.target.value })} />
                </div>
              ))}
              <div style={S.card()}>
                <label style={S.label}>😊 Mood Rating</label>
                <div style={{ display: "flex", alignItems: "center", gap: "14px", marginBottom: "16px" }}>
                  <input type="range" min="1" max="10" value={newRef.mood} onChange={e => setNewRef({ ...newRef, mood: +e.target.value })} style={{ flex: 1, accentColor: "#f5c518" }} />
                  <span style={{ fontSize: "28px", fontWeight: "800", color: newRef.mood >= 8 ? "#4ade80" : newRef.mood >= 5 ? "#f5c518" : "#ef6c4a" }}>{newRef.mood}</span>
                </div>
                <label style={S.label}>✨ One Word for This Week</label>
                <input style={S.input} placeholder="e.g. Focused, Unstoppable, Reset..." value={newRef.word} onChange={e => setNewRef({ ...newRef, word: e.target.value })} />
              </div>
              <div style={{ gridColumn: "1 / -1" }}>
                <button style={{ ...S.btn("gold"), padding: "14px 32px", fontSize: "15px" }} onClick={() => {
                  setReflections(r => [...r, { ...newRef, week: weekNum(today()), date: today() }]);
                  setNewRef({ wins: "", struggles: "", adjust: "", mood: 7, word: "" });
                  showToast("Reflection saved! 🏆 Week " + weekNum(today()));
                }}>Save Week {weekNum(today())} Reflection →</button>
              </div>
              {reflections.length > 0 && (
                <div style={{ ...S.card(), gridColumn: "1 / -1" }}>
                  <div style={S.label}>PAST REFLECTIONS</div>
                  {[...reflections].reverse().map((r, i) => (
                    <div key={i} style={{ display: "flex", alignItems: "center", gap: "14px", padding: "12px 0", borderBottom: "1px solid #1a1a1a" }}>
                      <div style={{ ...S.mono, fontSize: "12px", color: "#f5c518", fontWeight: "700" }}>W{r.week}</div>
                      <div style={{ ...S.mono, fontSize: "12px", color: "#888", flex: 1 }}>"{r.word || '—'}"</div>
                      <div style={{ fontSize: "20px", fontWeight: "800", color: r.mood >= 8 ? "#4ade80" : r.mood >= 5 ? "#f5c518" : "#ef6c4a" }}>{r.mood}/10</div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        )}
      </div>

      {/* Add Habit Modal */}
      {showAdd && (
        <div style={S.modal} onClick={e => e.target === e.currentTarget && setShowAdd(false)}>
          <div style={S.modalBox}>
            <div style={{ fontSize: "20px", fontWeight: "800", color: "#fff", marginBottom: "6px" }}>Add to Your Stack</div>
            <div style={{ ...S.mono, fontSize: "11px", color: "#555", marginBottom: "24px" }}>Custom habit or choose from library</div>
            <div style={{ marginBottom: "14px" }}>
              <label style={S.label}>Habit Name</label>
              <input style={S.input} placeholder="e.g. 5 AM wake-up" value={newHabit.name} onChange={e => setNewHabit({ ...newHabit, name: e.target.value })} onKeyDown={e => e.key === "Enter" && addHabit()} autoFocus />
            </div>
            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "12px", marginBottom: "14px" }}>
              {[
                { key: "category", label: "Category", opts: CATEGORIES },
                { key: "difficulty", label: "Difficulty", opts: ["Easy", "Medium", "Hard"] },
                { key: "time", label: "Time of Day", opts: ["Morning", "Afternoon", "Evening", "Anytime"] },
                { key: "icon", label: "Icon", opts: ["⭐","🔥","💪","🧠","📚","💧","🏃","🎯","✍️","🧘","💰","❤️","🌙","⚡","🛡️"] },
              ].map(field => (
                <div key={field.key}>
                  <label style={S.label}>{field.label}</label>
                  <select style={{ ...S.select, width: "100%" }} value={newHabit[field.key]} onChange={e => setNewHabit({ ...newHabit, [field.key]: e.target.value })}>
                    {field.opts.map(o => <option key={o} value={o}>{o}</option>)}
                  </select>
                </div>
              ))}
            </div>
            <div style={{ display: "flex", gap: "10px", justifyContent: "flex-end", marginTop: "20px" }}>
              <button style={S.btn("outline")} onClick={() => setShowAdd(false)}>Cancel</button>
              <button style={S.btn("gold")} onClick={addHabit}>Add to Stack</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
