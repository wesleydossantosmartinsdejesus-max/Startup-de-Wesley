package com.example.ciclomultfood.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.ciclomultfood.data.model.UserPreferencesEntity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object EsgCertificateExporter {

    private fun generateCertificateBitmap(userPrefs: UserPreferencesEntity): Bitmap {
        val width = 1200
        val height = 850
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Colors
        val bgPaint = Paint().apply { color = Color.parseColor("#FAF9F6"); style = Paint.Style.FILL }
        val primaryPaint = Paint().apply { color = Color.parseColor("#2E7D32"); isAntiAlias = true }
        val goldPaint = Paint().apply { color = Color.parseColor("#D4AF37"); isAntiAlias = true }
        val darkTextPaint = Paint().apply { color = Color.parseColor("#1B2A1E"); isAntiAlias = true }
        val grayTextPaint = Paint().apply { color = Color.parseColor("#556B5A"); isAntiAlias = true }

        // Background
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // Outer Double Borders
        val outerBorder = Paint().apply {
            color = Color.parseColor("#2E7D32")
            style = Paint.Style.STROKE
            strokeWidth = 12f
            isAntiAlias = true
        }
        val innerBorder = Paint().apply {
            color = Color.parseColor("#D4AF37")
            style = Paint.Style.STROKE
            strokeWidth = 4f
            isAntiAlias = true
        }

        canvas.drawRect(24f, 24f, (width - 24).toFloat(), (height - 24).toFloat(), outerBorder)
        canvas.drawRect(36f, 36f, (width - 36).toFloat(), (height - 36).toFloat(), innerBorder)

        // Top Header Banner
        val headerBox = RectF(60f, 60f, (width - 60).toFloat(), 160f)
        val headerBg = Paint().apply { color = Color.parseColor("#E8F5E9"); style = Paint.Style.FILL }
        canvas.drawRoundRect(headerBox, 16f, 16f, headerBg)

        primaryPaint.apply {
            textSize = 28f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }
        canvas.drawText("CICLOMULT_FOOD • PLATAFORMA SUSTENTÁVEL", width / 2f, 105f, primaryPaint)

        goldPaint.apply {
            textSize = 16f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }
        canvas.drawText("CERTIFICADO ESG & IMPACTO AMBIENTAL DE COMUNIDADE", width / 2f, 140f, goldPaint)

        // Certificate Title
        darkTextPaint.apply {
            textSize = 42f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }
        canvas.drawText("CERTIFICADO DE RECONHECIMENTO", width / 2f, 230f, darkTextPaint)

        grayTextPaint.apply {
            textSize = 18f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("Atestamos oficialmente para fins de impacto socioambiental positivo que o(a) integrante:", width / 2f, 270f, grayTextPaint)

        // User Name & Handle
        primaryPaint.apply {
            textSize = 40f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }
        val nameText = userPrefs.userName.ifBlank { "Integrante da Comunidade" }
        canvas.drawText(nameText, width / 2f, 335f, primaryPaint)

        grayTextPaint.apply {
            textSize = 22f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }
        val handleText = "${userPrefs.userHandle} • ${userPrefs.userNeighborhood}"
        canvas.drawText(handleText, width / 2f, 375f, grayTextPaint)

        grayTextPaint.apply {
            textSize = 16f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = false
        }
        canvas.drawText("contribuiu ativamente no combate ao desperdício de alimentos, registrando o seguinte impacto:", width / 2f, 415f, grayTextPaint)

        // Impact Cards Box (Grid of 3)
        val cardYStart = 450f
        val cardHeight = 160f
        val cardWidth = 320f
        val gap = 30f
        val totalCardsWidth = (cardWidth * 3) + (gap * 2)
        val cardXStart = (width - totalCardsWidth) / 2f

        val cardBgPaint = Paint().apply { color = Color.parseColor("#FFFFFF"); style = Paint.Style.FILL }
        val cardStrokePaint = Paint().apply {
            color = Color.parseColor("#C8E6C9")
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }

        val metrics = listOf(
            Triple("Alimentos Salvos", "%.1f kg".format(userPrefs.totalKgSaved), "🥦 Preservação"),
            Triple("CO2 Evitado", "%.1f kg".format(userPrefs.totalCo2EvadedKg), "🌍 Sustentabilidade"),
            Triple("Doações Realizadas", "${userPrefs.donationsCompleted} ações", "❤️ Solidariedade")
        )

        metrics.forEachIndexed { index, (label, valText, tag) ->
            val xLeft = cardXStart + index * (cardWidth + gap)
            val rect = RectF(xLeft, cardYStart, xLeft + cardWidth, cardYStart + cardHeight)

            canvas.drawRoundRect(rect, 16f, 16f, cardBgPaint)
            canvas.drawRoundRect(rect, 16f, 16f, cardStrokePaint)

            primaryPaint.textSize = 28f
            primaryPaint.textAlign = Paint.Align.CENTER
            primaryPaint.isFakeBoldText = true
            canvas.drawText(valText, xLeft + cardWidth / 2f, cardYStart + 60f, primaryPaint)

            darkTextPaint.textSize = 18f
            darkTextPaint.textAlign = Paint.Align.CENTER
            darkTextPaint.isFakeBoldText = true
            canvas.drawText(label, xLeft + cardWidth / 2f, cardYStart + 100f, darkTextPaint)

            goldPaint.textSize = 14f
            goldPaint.textAlign = Paint.Align.CENTER
            goldPaint.isFakeBoldText = true
            canvas.drawText(tag, xLeft + cardWidth / 2f, cardYStart + 135f, goldPaint)
        }

        // Corporate ESG Sponsors Banner
        val sponsorBox = RectF(100f, 635f, (width - 100).toFloat(), 715f)
        val sponsorBg = Paint().apply { color = Color.parseColor("#F1F8E9"); style = Paint.Style.FILL }
        canvas.drawRoundRect(sponsorBox, 12f, 12f, sponsorBg)

        grayTextPaint.textSize = 14f
        grayTextPaint.textAlign = Paint.Align.CENTER
        grayTextPaint.isFakeBoldText = true
        canvas.drawText("PATROCÍNIO CORPORATIVO - COTAS ESG", width / 2f, 665f, grayTextPaint)

        primaryPaint.textSize = 18f
        primaryPaint.textAlign = Paint.Align.CENTER
        primaryPaint.isFakeBoldText = true
        canvas.drawText("🏆 Supermercados VerdeMais ESG • BioAdubo Brasil", width / 2f, 698f, primaryPaint)

        // Footer Metadata & Verification
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateStr = dateFormat.format(Date())
        val verificationCode = "ESG-2026-CMF-${userPrefs.userHandle.hashCode().toString().takeLast(6).uppercase()}"

        grayTextPaint.textSize = 13f
        grayTextPaint.textAlign = Paint.Align.LEFT
        grayTextPaint.isFakeBoldText = false
        canvas.drawText("Emitido em: $dateStr • Ciclomult_Food App", 100f, 770f, grayTextPaint)

        grayTextPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText("Autenticidade: #$verificationCode", (width - 100).toFloat(), 770f, grayTextPaint)

        return bitmap
    }

    fun exportAsImage(context: Context, userPrefs: UserPreferencesEntity) {
        try {
            val bitmap = generateCertificateBitmap(userPrefs)
            val cacheDir = File(context.cacheDir, "certificates").apply { mkdirs() }
            val file = File(cacheDir, "Certificado_ESG_${userPrefs.userHandle.replace("@", "")}.png")

            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }

            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "🌱 Confira meu Certificado de Impacto ESG no Ciclomult_Food!")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "Emitir Certificado (Imagem PNG)"))
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Erro ao gerar imagem do certificado: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun exportAsPdf(context: Context, userPrefs: UserPreferencesEntity) {
        try {
            val bitmap = generateCertificateBitmap(userPrefs)

            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
            val page = pdfDocument.startPage(pageInfo)

            val canvas = page.canvas
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            pdfDocument.finishPage(page)

            val cacheDir = File(context.cacheDir, "certificates").apply { mkdirs() }
            val file = File(cacheDir, "Certificado_ESG_${userPrefs.userHandle.replace("@", "")}.pdf")

            FileOutputStream(file).use { out ->
                pdfDocument.writeTo(out)
            }
            pdfDocument.close()

            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "🌱 Confira meu Certificado de Impacto ESG em PDF no Ciclomult_Food!")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "Emitir Certificado (PDF)"))
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Erro ao gerar PDF do certificado: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
