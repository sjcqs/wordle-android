import * as admin from "firebase-admin";
import * as functions from "firebase-functions";

admin.initializeApp(functions.config().firebase);

const REF_ALL_WORDS = "/all_words";
const REF_WORDS = "/words";
const REF_DAILY_WORD = "/daily_word";

const functionBuilder = functions.region("europe-west1");

async function updateDailyWord() {
  const wordsRef = admin.database()
      .ref(REF_WORDS)
      .orderByKey();
  const dailyWordRef = admin.database()
      .ref(REF_DAILY_WORD);
  return wordsRef.limitToFirst(1)
      .get()
      .then((snapshot) => {
        for (const key in snapshot.val()) {
          if (snapshot.val()[key] !== undefined) {
            const word = snapshot.val()[key];
            console.log(word);
            snapshot.ref.child(key).remove();
            const date = expiredAt();
            dailyWordRef.update({
              expired_at: date,
              word: word,
            });
          }
        }
      });
}

function expiredAt(): Date {
  const date = new Date();
  date.setDate(date.getUTCDate() + 1);
  date.setHours(0, 0, 0, 0);
  return date;
}

exports.importWords = functionBuilder.https.onRequest(async (_, resp) => {
  const ref = admin.database().ref(REF_ALL_WORDS).orderByKey();
  const wordsRef = admin.database().ref(REF_WORDS);

  await wordsRef.remove();
  await ref.once("value", async (snap) => {
    snap.forEach((child) => {
      const word = child.val();
      wordsRef.push(word);
    });
  });
  resp.status(200).send("OK");
});

exports.scheduleUpdateDailyWord = functionBuilder.pubsub
    .schedule("every day 00:00")
    .timeZone("Europe/Paris")
    .onRun(async () => {
      return updateDailyWord();
    });
