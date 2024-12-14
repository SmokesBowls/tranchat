import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import ConversationManager from './components/ConversationManager';
import Chat from './components/Chat';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<ConversationManager />} />
        <Route path="/chat" element={<Chat />} />
      </Routes>
    </Router>
  );
}

export default App; 